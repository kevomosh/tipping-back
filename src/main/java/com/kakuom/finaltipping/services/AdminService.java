package com.kakuom.finaltipping.services;


import com.kakuom.finaltipping.dto.GameDTO;
import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.responses.BasicResponse;
import com.kakuom.finaltipping.views.*;

import java.time.OffsetDateTime;
import java.util.List;

public interface AdminService {
    BasicResponse createGroups(StringSetView view, Comp comp);

    BasicResponse createTeams(StringSetView view, Comp comp);

    BasicResponse addPlayersToTeam(AddPlayersView addPlayersView, Comp comp);

    BasicResponse createWeek(CreateWeekView createWeekView, Comp comp);

    OffsetDateTime changeDeadline(DateView dateView, Integer weekNumber, Comp comp);

    BasicResponse addResults(Integer weekNumber, ResultView resultView, Comp comp);

    List<GameDTO> getGamesToUpdateResult(Integer weekNumber, Comp comp);

    BasicResponse updateTotalScore(Integer weekNumber, Comp comp);

    List<String> getAllTeamsByComp(Comp comp);


}
