package com.kakuom.finaltipping.services;

import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.responses.BasicResponse;
import com.kakuom.finaltipping.responses.GamesForWeek;
import com.kakuom.finaltipping.responses.PicksForWeek;
import com.kakuom.finaltipping.responses.ResultsForWeek;
import com.kakuom.finaltipping.views.PickView;
import java.util.Set;

public interface UserService {
    GamesForWeek getGamesForWeek(Integer weekNumber, Comp comp);

    GamesForWeek getLatestGames(Comp comp);

    BasicResponse createPick(PickView pickView, Comp comp);

    PicksForWeek getPicksForWeekNumber(Integer weekNumber,
                                       Comp comp, Set<Long> gid,
                                       String name,
                                       int page, int size);

    PicksForWeek getPicksForLatestWeek(Comp comp, Set<Long> gid,
                                       String name,
                                       int page, int size);

    ResultsForWeek getResultsForWeek(Comp comp,
                                      Set<Long> gid,
                                     String name,
                                     int page, int size, String[] sort);

}
