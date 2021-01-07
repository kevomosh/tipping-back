package com.kakuom.finaltipping.services;

import com.kakuom.finaltipping.dto.ResultDTO;
import com.kakuom.finaltipping.dto.WeekInfoDTO;
import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.model.Pick;
import com.kakuom.finaltipping.model.Selected;
import com.kakuom.finaltipping.repositories.*;
import com.kakuom.finaltipping.responses.BasicResponse;
import com.kakuom.finaltipping.responses.GamesForWeek;
import com.kakuom.finaltipping.responses.PicksForWeek;
import com.kakuom.finaltipping.responses.ResultsForWeek;
import com.kakuom.finaltipping.security.UserPrincipal;
import com.kakuom.finaltipping.views.PickView;
import com.kakuom.finaltipping.views.SelectedView;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final WeekRepository weekRepository;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final GroupRepository groupRepository;
    private final ResultRepository resultRepository;
    private final UserRepository userRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final PickRepository pickRepository;

    public UserServiceImpl(WeekRepository weekRepository, GameRepository gameRepository,
                           PlayerRepository playerRepository, GroupRepository groupRepository,
                           ResultRepository resultRepository, UserRepository userRepository,
                           EntityManagerFactory entityManagerFactory,
                           PickRepository pickRepository) {
        this.weekRepository = weekRepository;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.groupRepository = groupRepository;
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
        this.entityManagerFactory = entityManagerFactory;
        this.pickRepository = pickRepository;
    }

    @Override
    public BasicResponse createPick(PickView pickView, Comp comp) {

        var weekNumber = Math.max(1,pickView.getWeekNumber());
        var userId = this.getCurrentUserId();
        if (!groupRepository.isInComp(userId, comp.getComp())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please join relevant comp inorder to make picks");
        }

        var isBeforeDeadLine = weekRepository.getDeadlineForWeekNumber(weekNumber, comp)
                .map(OffsetDateTime.now()::isBefore)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        comp.getComp() + " week not yet in records"));

        if (gameRepository.getLimit(weekNumber, comp).intValue() != pickView.getSelectedViewList().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must make pick for all games in the week");
        }


        if (isBeforeDeadLine) {
            var user = userRepository.getById(userId).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.BAD_REQUEST, "You dont exist in the records"));

            user.getPicks()
                    .stream()
                    .filter(p -> p.getWeekNumber().equals(weekNumber) && p.getComp().equals(comp))
                    .findFirst()
                    .ifPresentOrElse(pick -> {
                        for (Selected initial : pick.getTeamsSelected()) {
                            for (SelectedView view : pickView.getSelectedViewList()) {
                                var number = view.getGameNumber();
                                var sameNumber = number.equals(initial.getGameNumber());
                                if (sameNumber) {
                                    initial.setTeam(view.getTeam());
                                }
                            }
                        }
                        var firstScorer = pickView.getFirstScorer();
                        var margin = pickView.getMargin();
                        if (firstScorer != null)
                            pick.setFirstScorer(firstScorer);
                        if (margin != null)
                            pick.setMargin(margin);

                        pickRepository.save(pick);
                    }, ()-> {
                        var newPick = new Pick(weekNumber, comp, user.getName(),
                                pickView.getMargin(), pickView.getFirstScorer());

                        var limit = gameRepository.getLimit(weekNumber, comp);

                        pickView.getSelectedViewList().stream()
                                .limit(limit)
                                .map(selectedView -> new Selected(selectedView.getGameNumber(), selectedView.getTeam()))
                                .forEach(newPick::addSelected);

                        user.addPick(newPick);
                        userRepository.save(user);

                    });

            return new BasicResponse(comp.getComp() + " pick for week " + weekNumber + " created or updated");

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Its after deadline for "
                    + comp.getComp() + " week " + weekNumber + "No more picks being taken ");
        }
    }

    //TODO fwp showing false when i choose picks for week onw
    @Override
    public PicksForWeek getPicksForWeekNumber(Integer weekNumber, Comp comp,
                                              Set<Long> gid, String name, int page,
                                              int size) {
        weekNumber = Math.max(weekNumber, 1);
        weekNumber = Math.min(weekNumber, 25);

        return getPicksForWeek(weekNumber, comp, gid, name, page, size);
    }

    @Override
    public PicksForWeek getPicksForLatestWeek(Comp comp, Set<Long> gid, String name, int page, int size) {
        var weekNumber = weekRepository.getLatestWeekNumber(comp.getComp()).intValue();
        return getPicksForWeek(weekNumber, comp, gid, name, page, size);
    }

    @NotNull
    private PicksForWeek getPicksForWeek(Integer weekNumber, Comp comp, Set<Long> gid, String name, int page, int size) {
        var userId = this.getCurrentUserId();
        var gameInfo = getWeekInfo(weekNumber, weekNumber + 1, comp.getComp()).get(0);


        if (OffsetDateTime.now().isBefore(gameInfo.getDeadLine())) {
            var singlePick = pickRepository.getPickBeforeDeadLine(weekNumber, comp, userId);
            if (singlePick.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Need to make a pick");
            } else {
                var y = singlePick.stream()
                        .peek(this::setPickIdsToNull)
                        .collect(Collectors.toList());
                return new PicksForWeek(y, gameRepository.getGamesForWeek(weekNumber, comp));
            }
        }

        var sameGroupUserIds = getRelevantIds(userId, gid, comp);
        size = Math.max(size, 5);
        size = Math.min(size, 20);

        page = Math.max(page, 0);

        Pageable pageable = PageRequest.of(page, size);

        Page<Long> pagedIds;
        if (name != null && name.strip().length() > 1) {
            pagedIds = pickRepository.getPickIdsWithName(weekNumber, comp, sameGroupUserIds, name.strip(), pageable);
        } else {
            pagedIds = pickRepository.getPickIds(weekNumber, comp, sameGroupUserIds, pageable);
        }

        var sorted = pickRepository.getPicksWithIds(pagedIds.getContent())
                .stream()
                .peek(this::setPickIdsToNull)
                .collect(Collectors.toList());

        return new PicksForWeek(pagedIds.getTotalElements(), pagedIds.getNumber(),gameInfo.getFwp()
                ,gameInfo.getFirstScorer(), gameInfo.getMargin(), sorted,
                resultRepository.findAllByWeekAndComp(weekNumber, comp), gameRepository.getGamesForWeek(weekNumber, comp));
    }



    private void setPickIdsToNull(Pick pick) {
        pick.setId(null);
        for (Selected selected: pick.getTeamsSelected()) {
            selected.setId(null);
        }

    }

    @Override
    public ResultsForWeek getResultsForWeek(Comp comp,
                                             Set<Long> gid,
                                            String name, int page, int size, String[] sort) {
      var   userId = getCurrentUserId();

        List<Long> actualUserIds = getRelevantIds(userId, gid, comp);

        size = Math.max(size, 1);
        size = Math.min(size, 20);

        page = Math.max(page, 0);

        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sort, comp.getComp())));

        Page<ResultDTO> pagedResults;

        if (name != null && name.strip().length() > 1) {
            if (comp.equals(Comp.NRL)) {
                pagedResults = userRepository.findNrlResultsByName(actualUserIds, name.strip(), pageable);
            } else {
                pagedResults = userRepository.findAflResultsByName(actualUserIds, name.strip(), pageable);
            }
        } else {
            if (comp.equals(Comp.NRL)) {
                pagedResults = userRepository.findNrlResults(actualUserIds, pageable);
            } else {
                pagedResults = userRepository.findAflResults(actualUserIds, pageable);
            }
        }
        var latestWeek = weekRepository.getLatestWeekNumber(comp.getComp());
        var t = pagedResults.getSize();
        return new ResultsForWeek(pagedResults.getTotalElements(),pagedResults.getNumber(),
                pagedResults.getSize(),pagedResults.getContent(), latestWeek);
    }

    private List<Long> getRelevantIds(Long userId, Set<Long> gid, Comp comp) {
        List<Long> actualUserIds;
        if (gid == null) {
            actualUserIds = groupRepository.getIdsInSameCompGroup(userId, comp.getComp());
        } else {
            actualUserIds = groupRepository.getIdsInCompGroup(gid, comp.getComp());
        }
        if (actualUserIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "wrong info being supplied, " +
                    "Please sign in again and retry");
        }
        return actualUserIds;
    }

    @Override
    public GamesForWeek getGamesForWeek(Integer weekNumber, Comp comp) {
        weekNumber = Math.max(weekNumber, 1);
        weekNumber = Math.min(weekNumber, 25);


        var gameInfo = getWeekInfo(weekNumber, weekNumber + 1, comp.getComp()).get(0);


        if (OffsetDateTime.now().isAfter(gameInfo.getDeadLine())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Its after deadline. Click " +
                    "on latest week button ");
        }
        return getActualGamesForWeek(weekNumber, comp, gameInfo);
    }

    @Override
    public GamesForWeek getLatestGames(Comp comp) {

        var weekNumber = weekRepository.getLatestWeekNumber(comp.getComp()).intValue();
        var gameInfo = getWeekInfo(weekNumber, weekNumber + 1, comp.getComp()).get(0);

        if (OffsetDateTime.now().isAfter(gameInfo.getDeadLine())) {
            var nextWeekNumber = weekNumber + 1;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Past deadline for latest week. " +
                    "Though you can make picks for the following week which is week number  " + nextWeekNumber);
        }
        return getActualGamesForWeek(weekNumber, comp, gameInfo);
    }


    private GamesForWeek getActualGamesForWeek(Integer weekNumber, Comp comp, WeekInfoDTO gameInfo) {
        var games = gameRepository.getGamesForWeek(weekNumber, comp);

        List<String> teamNames = new ArrayList<>();
        games.stream()
                .filter(g -> g.getGameNumber().equals(1))
                .findFirst()
                .ifPresent(gameDto -> {
                    teamNames.add(gameDto.getHomeTeam());
                    teamNames.add(gameDto.getAwayTeam());
                });

        return new GamesForWeek(gameInfo.getDeadLine(), gameInfo.getNumber(),
                gameInfo.getFwp(), games, playerRepository.getPlayersByTeamName(teamNames, comp));
    }

    private Long getCurrentUserId() {
        var user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         return user.getId();
    }

    private List<WeekInfoDTO> getWeekInfo(Integer weekNumber, Integer nextWeekNumber, String comp) {
        var em = entityManagerFactory.createEntityManager();

        Query r = em.createNativeQuery(
                "select w.dead_line, w.number ," +
                        " (select exists(select 1 where w.number = :nextWeekNumber))as nxt," +
                        " w.margin , w.first_scorer as fs from week w where w.number = :weekNumber and w.comp = :comp",
                "WeekInfoDtoMapping"
        ).setParameter("weekNumber", weekNumber)
                .setParameter("comp", comp)
                .setParameter("nextWeekNumber", nextWeekNumber);

        List<WeekInfoDTO> info = r.getResultList();
        em.close();
        if (info.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Week doesn't exists");

        return info;
    }

    private List<Sort.Order> createSortOrder(String[] sort, String comp) {
        List<Sort.Order> orders = new ArrayList<>();
        List<String> keyWords = List.of("ts", "ls");
        if(sort[0].contains(",")) {
            for (int i = 0; i < 2; i++) {
                String[] _sort = sort[i].split(",");
                if (_sort[0].equals(keyWords.get(1))) {
                    _sort[0] = comp + "LastScore";
                } else {
                    _sort[0] = comp + "TotalScore";
                }
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));

            }
        } else {
            if (sort[0].equals(keyWords.get(1))) {
                sort[0] = comp + "LastScore";
            } else {
                sort[0] = comp + "TotalScore";
            }
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }
        return orders;
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

}
