package com.kakuom.finaltipping.services;

import com.kakuom.finaltipping.dto.ResultDTO;
import com.kakuom.finaltipping.dto.WeekInfoDTO;
import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.model.Pick;
import com.kakuom.finaltipping.model.Selected;
import com.kakuom.finaltipping.repositories.*;
import com.kakuom.finaltipping.responses.BasicResponse;
import com.kakuom.finaltipping.responses.GamesForWeek;
import com.kakuom.finaltipping.responses.ResultsForWeek;
import com.kakuom.finaltipping.views.PickView;
import com.kakuom.finaltipping.views.SelectedView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
        if (!groupRepository.isInComp(pickView.getUserId(), comp.getComp())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please join afl comp inorder to make picks");
        }

        var weekNumber = pickView.getWeekNumber();

        var isBeforeDeadLine = weekRepository.getDeadlineForWeekNumber(weekNumber, comp)
                .map(OffsetDateTime.now()::isBefore)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        comp.getComp() + " week not yet in records"));

        if (isBeforeDeadLine) {
            return userRepository.findById(pickView.getUserId())
                    .map(user -> {
                        pickRepository.findByWeekNumberAndUserId(weekNumber, comp, user.getId())
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
                                }, () -> {


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
                    })
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "You dont exist in the records"));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist");
        }
    }


    @Override
    public Map<String, Object> getPicksForWeekNumber(Long userId, Integer weekNumber, Comp comp,
                                                     Set<Long> gid, String name, int page, int size) {
        var gameInfo = getWeekInfo(weekNumber, weekNumber + 1, comp.getComp()).get(0);

        Map<String, Object> response = new HashMap<>();

        if (OffsetDateTime.now().isBefore(gameInfo.getDeadLine())) {
            var singlePick = pickRepository.getPickBeforeDeadLine(weekNumber, comp, userId);
            if (singlePick.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Need to make a pick");
            } else {
                response.put("picks", singlePick);
                return response;
            }
        }

        var sameGroupUserIds = getRelevantIds(userId, gid, comp);
        size = Math.max(size, 5);
        size = Math.min(size, 20);

        page = Math.min(0, page);
        Pageable pageable = PageRequest.of(page, size);

        Page<Long> pagedIds;
        if (name != null && name.strip().length() > 1) {
            pagedIds = pickRepository.getPickIdsWithName(weekNumber, comp, sameGroupUserIds, name.strip(), pageable);
        } else {
            pagedIds = pickRepository.getPickIds(weekNumber, comp, sameGroupUserIds, pageable);
        }

        var sorted = pickRepository.getPicksWithIds(pagedIds.getContent())
                .stream()
                .peek(s -> {
                    if (s != null) {
                        s.getTeamsSelected().sort(Comparator.nullsLast(Comparator.naturalOrder()));
                    }
                })
                .collect(Collectors.toList());

        response.put("firstScorer", gameInfo.getFirstScorer());
        response.put("margin", gameInfo.getMargin());
        response.put("fwp", gameInfo.getFwp());
        response.put("picks", sorted);
        response.put("total", pagedIds.getTotalElements());
        response.put("winners", resultRepository.findAllByWeekAndComp(weekNumber, comp));

        return response;
    }

    @Override
    public ResultsForWeek getResultsForWeek(Comp comp,
                                            Long userId, Set<Long> gid,
                                            String name, int page, int size) {

        List<Long> actualUserIds = getRelevantIds(userId, gid, comp);

        size = Math.max(size, 5);
        size = Math.min(size, 20);

        page = Math.min(0, page);

        Pageable pageable = PageRequest.of(page, size);

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

        return new ResultsForWeek(pagedResults.getTotalElements(), pagedResults.getContent());
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
                    "Though you can make picks for next week " + nextWeekNumber);
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
                    teamNames.add(gameDto.getAwayTeam());
                    teamNames.add(gameDto.getAwayTeam());
                });

        return new GamesForWeek(gameInfo.getDeadLine(), gameInfo.getNumber(),
                gameInfo.getFwp(), games, playerRepository.getPlayersByTeamName(teamNames, comp));
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

}
