package com.kakuom.finaltipping.services;

import com.kakuom.finaltipping.dto.GameDTO;
import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.model.*;
import com.kakuom.finaltipping.repositories.*;
import com.kakuom.finaltipping.responses.BasicResponse;
import com.kakuom.finaltipping.views.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final GroupRepository groupRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final WeekRepository weekRepository;
    private final ResultRepository resultRepository;
    private final GameRepository gameRepository;
    private final PickRepository pickRepository;
    private final UserRepository userRepository;

    public AdminServiceImpl(GroupRepository groupRepository, TeamRepository teamRepository,
                            PlayerRepository playerRepository, WeekRepository weekRepository,
                            ResultRepository resultRepository, GameRepository gameRepository,
                            PickRepository pickRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.weekRepository = weekRepository;
        this.resultRepository = resultRepository;
        this.gameRepository = gameRepository;
        this.pickRepository = pickRepository;
        this.userRepository = userRepository;
    }


    @Override
    public BasicResponse addResults(Integer weekNumber, ResultView resultView, Comp comp) {
        var week = weekRepository.findByNumber(weekNumber, comp).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Week doesn't exist"));

        if (OffsetDateTime.now().isBefore(week.getDeadLine()) || week.getScoreUpdated()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Its before deadline. Tulia. or total scores have already been updated");
        }
        if (week.getMargin() != null) {
            resultView.setMargin(null);
        }
        if (week.getFirstScorer() != null) {
            resultView.setFirstScorer(null);
        }

        var gameNumberResultsAlreadyIn = resultRepository.getGameNumbersForWeek(week.getId());

        List<Integer> gameNumberList = new ArrayList<>();
        List<Result> resultList = new ArrayList<>();

        resultView.getResultViewSet().stream()
                .filter(g -> !gameNumberResultsAlreadyIn.contains(g.getGameNumber()))
                .map(r -> new Result(r.getGameNumber(), r.getTeam()))
                .sorted(Comparator.comparingInt(Result::getGameNumber))
                .forEach(z -> {
                    gameNumberList.add(z.getGameNumber());
                    week.addResult(z);
                    resultList.add(z);
                });

        if (resultList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Results you want to add already " +
                    "added before");
        }

        if (gameNumberList.contains(2) && resultView.getMargin() != null) {
            week.setMargin(resultView.getMargin());
        }
        if (gameNumberList.contains(1) && resultView.getFirstScorer() != null) {
            week.setFirstScorer(resultView.getFirstScorer());
        }

        weekRepository.save(week);

        var allPicksForWeek = pickRepository.getAllWithWeekNumber(weekNumber, comp);

        for (Pick pick : allPicksForWeek) {
            var score = 0;
            var extraPoint = 0;

            var relevantSelected = pick.getTeamsSelected()
                    .stream()
                    .filter(s -> gameNumberList.contains(s.getGameNumber()))
                    .collect(Collectors.toList());

            for (Result result : resultList) {
                for (Selected selected : relevantSelected) {
                    var rgn = result.getGameNumber();
                    var sgn = selected.getGameNumber();

                    if (rgn.equals(sgn)) {
                        if (rgn.equals(2) &&
                                week.getMargin().equals(pick.getMargin()) &&
                                result.getTeam().equals(selected.getTeam())
                        ) {
                            extraPoint = extraPoint + 5;
                            score = score + 1;
                        } else if (rgn.equals(2) && result.getTeam().equals(selected.getTeam())) {
                            score = score + 1;
                        } else if (rgn.equals(1) &&
                                week.getFirstScorer().equals(pick.getFirstScorer()) &&
                                result.getTeam().equals(selected.getTeam())) {
                            extraPoint = extraPoint + 3;
                            score = score + 1;

                        } else if (rgn.equals(1) && week.getFirstScorer().equals(pick.getFirstScorer())) {
                            extraPoint = extraPoint + 3;
                        } else if (rgn.equals(1) && result.getTeam().equals(selected.getTeam())) {
                            score = score + 1;
                        } else if (result.getTeam().equals(selected.getTeam())) {
                            score = score + 1;
                        }

                        break;
                    }

                }
            }
            pick.setExtraPoint(pick.getExtraPoint() + extraPoint);
            pick.setScore(pick.getScore() + score + extraPoint);
            pickRepository.save(pick);
        }

        return new BasicResponse("Results added");
    }

    @Override
    public List<GameDTO> getGamesToUpdateResult(Integer weekNumber, Comp comp) {
        var deadLine = weekRepository.getDeadlineForWeekNumber(weekNumber, comp)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Week doesn't exist"));

        if (OffsetDateTime.now().isBefore(deadLine)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Before deadLine, relax");
        }
        return gameRepository.getGamesToUpdateResult(weekNumber, comp);

    }

    @Override
    public BasicResponse updateTotalScore(Integer weekNumber, Comp comp) {
        var updated = weekRepository.checkScoreUpdated(weekNumber, comp);
        if (updated == null || updated) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Week doesnt exist or total score already updated");
        }

        var allResultsEntered =
                weekRepository.checkAllResultsEntered(weekRepository.getWeekId(weekNumber, comp));
        if (!allResultsEntered) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All results haven't been entered");
        }

        var idList = userRepository.getAllIdsForUsersMadePick(weekNumber, comp.getComp());

        var relevantUsers = userRepository.getUsersThatMadePick(idList);
        for (User user : relevantUsers) {
            user.getPicks()
                    .stream()
                    .filter(pick -> pick.getWeekNumber().equals(weekNumber) && pick.getComp().equals(comp))
                    .findFirst()
                    .ifPresent(p -> {
                        if (p.getComp().equals(Comp.NRL)) {
                            user.setNrlLastScore(p.getScore());
                            user.setNrlTotalScore(user.getNrlTotalScore() + p.getScore());
                        } else {
                            user.setAflLastScore(p.getScore());
                            user.setAflTotalScore(user.getAflTotalScore() + p.getScore());
                        }
                        userRepository.save(user);
                    });

        }
        var nonRelevantUsers = userRepository.getUsersWithNoPick(idList);
        nonRelevantUsers.stream()
                .peek(user -> {
                    if (comp.equals(Comp.NRL)) {
                        user.setNrlLastScore(0);
                    } else {
                        user.setAflLastScore(0);
                    }
                }).forEach(userRepository::save);
        weekRepository.updateScoreUpdated(weekNumber, comp);
        return new BasicResponse("Total Score for the week updated");
    }

    @Override
    public List<String> getAllTeamsByComp(Comp comp) {
        return teamRepository.getAllTeamsByComp(comp);
    }

    @Override
    public BasicResponse createGroups(StringSetView view, Comp comp) {
        List<String> errorList = new ArrayList<>();
        for (String groupName : view.getStringSet()) {
            if (groupRepository.existsByNameAndComp(groupName, comp.getComp())) {
                errorList.add(groupName);
            } else {
                var newGroup = new Groups(groupName, comp);
                groupRepository.save(newGroup);
            }
        }

        if (errorList.isEmpty()) {
            return new BasicResponse("all names supplied created as groups");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " Following groups have " +
                    "not been added " + errorList.toString());
        }

    }

    @Override
    public BasicResponse createTeams(StringSetView view, Comp comp) {
        List<String> errorList = new ArrayList<>();
        for (String teamName : view.getStringSet()) {
            if (teamRepository.existsByNameAndComp(teamName, comp.getComp())) {
                errorList.add(teamName);
            } else {
                var newTeam = new Team(teamName, comp);
                teamRepository.save(newTeam);
            }
        }
        if (errorList.isEmpty()) {
            return new BasicResponse("All Teams supplied have been added");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " Following groups have " +
                    "not been added " + errorList.toString());
        }
    }

    @Override
    public BasicResponse addPlayersToTeam(AddPlayersView addPlayersView, Comp comp) {
        List<String> errorList = new ArrayList<>();
        teamRepository.findByNameAndComp(addPlayersView.getTeamName(), comp)
                .ifPresent(team -> {
                    for (PlayerView playerView : addPlayersView.getPlayers()) {
                        var firstName = playerView.getFirstName();
                        var lastName = playerView.getLastName();
                        if (playerRepository.existsByFirstNameAndLastNameAndTeamNameAndTeamComp(
                                firstName, lastName, team.getName(), comp
                        )) {
                            errorList.add(firstName + " " + lastName + " " + team.getName());
                            } else {
                                var player = new Player(firstName, lastName);
                                team.addPlayer(player);
                            }
                        }
                        teamRepository.save(team);
                    });

        if (errorList.isEmpty()) {
            return new BasicResponse("All Teams supplied have been added");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " Following  have " +
                    "not been added " + errorList.toString());
        }
    }

    @Override
    public BasicResponse createWeek(CreateWeekView createWeekView, Comp comp) {
        var weekNumber = createWeekView.getWeekNumber();
        if (weekRepository.existsByNumber(weekNumber, comp.getComp())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Week " + weekNumber + "for "
                    + comp.getComp() + " already exists. ");

        }
        var dateView = createWeekView.getDateView();
        OffsetDateTime deadLine = getDeadLine(dateView);

        var newWeek = new Week(weekNumber, deadLine, comp);
        createWeekView.getGamesToPlay()
                .stream()
                .distinct()
                .map(game -> new Game(game.getGameNumber(), game.getHomeTeam(), game.getAwayTeam()))
                .forEach(newWeek::addGame);

        weekRepository.save(newWeek);

        return new BasicResponse(comp.getComp() + " week number " + newWeek.getNumber() + " created");
    }


    @Override
    public OffsetDateTime changeDeadline(DateView dateView, Integer weekNumber, Comp comp) {
        var week = weekRepository.findByNumber(weekNumber, comp)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Week number " + weekNumber + " doesn't exist"
                ));

        if (week.getScoreUpdated()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scores already updated");
        }
        var deadLine = getDeadLine(dateView);
        week.setDeadLine(deadLine);
        weekRepository.save(week);
        return week.getDeadLine();

    }

    private OffsetDateTime getDeadLine(DateView dateView) {
        return OffsetDateTime.of(dateView.getYear(), dateView.getMonth(), dateView.getDay(), dateView.getHour(),
                dateView.getMinute(), 0, 0, ZoneOffset.of(dateView.getOffset()));
    }

}
