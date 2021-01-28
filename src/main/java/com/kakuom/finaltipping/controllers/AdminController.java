package com.kakuom.finaltipping.controllers;

import com.kakuom.finaltipping.dto.GameDTO;
import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.responses.BasicResponse;
import com.kakuom.finaltipping.responses.GamesForWeek;
import com.kakuom.finaltipping.responses.WeekNumberAllTeams;
import com.kakuom.finaltipping.services.AdminService;
import com.kakuom.finaltipping.views.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/")
@CrossOrigin
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @PostMapping("{competition}/createGroups")
    public BasicResponse createGroups(@PathVariable String competition,
                                      @Valid @RequestBody StringSetView view) {
        return adminService.createGroups(view, this.getComp(competition));
    }

    @PostMapping("{competition}/createTeams")
    public BasicResponse createTeams(
            @PathVariable String competition,
            @Valid @RequestBody StringSetView view
    ) {
        return adminService.createTeams(view, this.getComp(competition));
    }


    @PostMapping("{competition}/addPlayersToTeams")
    public BasicResponse addPlayers(
            @PathVariable String competition,
            @Valid @RequestBody AddPlayersView addPlayersView
    ) {
        return adminService.addPlayersToTeam(addPlayersView, this.getComp(competition));
    }

    @PostMapping("{competition}/createWeek")
    public BasicResponse createWeek(@PathVariable String competition,
                                    @Valid @RequestBody CreateWeekView createWeekView) {
        return adminService.createWeek(createWeekView, this.getComp(competition));
    }

    @PutMapping("{competition}/changeDeadLine/{weekNumber}")
    public OffsetDateTime changeDeadLine(@PathVariable String competition,
                                         @PathVariable Integer weekNumber,
                                         @Valid @RequestBody DateView dateView) {
        return adminService.changeDeadline(dateView, weekNumber, this.getComp(competition));
    }

    @PostMapping("{competition}/addResults")
    public BasicResponse addResults(@PathVariable String competition,
                                    @Valid @RequestBody ResultView resultView) {
        return adminService.addResults(resultView, this.getComp(competition));
    }

    @GetMapping("{competition}/gamesToUpdate")
    public GamesForWeek getGamesToUpdate(@PathVariable String competition) {
        return adminService.getGamesToUpdateResult(this.getComp(competition));
    }

    @PutMapping("{competition}/updateTotalScore")
    public BasicResponse updateTotalScore(@PathVariable String competition) {
        return adminService.updateTotalScore(this.getComp(competition));
    }

    @GetMapping("{competition}/getAllTeams")
    public WeekNumberAllTeams getAllTeams(@PathVariable String competition) {
        return adminService.getAllTeamsByComp(this.getComp(competition));
    }

    private Comp getComp(String competition) {
        return competition.equals("nrl") ? Comp.NRL : Comp.AFL;
    }
}

