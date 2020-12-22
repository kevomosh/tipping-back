package com.kakuom.finaltipping.services;

import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.responses.BasicResponse;
import com.kakuom.finaltipping.responses.GamesForWeek;
import com.kakuom.finaltipping.responses.ResultsForWeek;
import com.kakuom.finaltipping.views.PickView;

import java.util.Map;
import java.util.Set;

public interface UserService {
    GamesForWeek getGamesForWeek(Integer weekNumber, Comp comp);

    GamesForWeek getLatestGames(Comp comp);

    BasicResponse createPick(PickView pickView, Comp comp);

    Map<String, Object> getPicksForWeekNumber(Long userId, Integer weekNumber,
                                              Comp comp, Set<Long> gid,
                                              String name,
                                              int page, int size);

    ResultsForWeek getResultsForWeek(Comp comp,
                                     Long userId, Set<Long> gid,
                                     String name,
                                     int page, int size);

}
