package com.kakuom.finaltipping.controllers;

import com.kakuom.finaltipping.dto.GameDTO;
import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.responses.BasicResponse;
import com.kakuom.finaltipping.services.AdminService;
import com.kakuom.finaltipping.views.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/nrl/admin/")
@CrossOrigin
@PreAuthorize("hasRole('ADMIN')")
public class NrlAdminController {
    private final AdminService adminService;

    public NrlAdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("createGroups")
    public BasicResponse createGroups(@Valid @RequestBody StringSetView view) {
        return adminService.createGroups(view, Comp.NRL);
    }

    @PostMapping("createTeams")
    public BasicResponse createTeams(
            @Valid @RequestBody StringSetView view
    ) {
        return adminService.createTeams(view, Comp.NRL);
    }


    @PostMapping("addPlayersToTeams")
    public BasicResponse addPlayers(
            @Valid @RequestBody AddPlayersView addPlayersView
    ) {
        return adminService.addPlayersToTeam(addPlayersView, Comp.NRL);
    }

    @PostMapping("createWeek")
    public BasicResponse createWeek(@Valid @RequestBody CreateWeekView createWeekView) {
        return adminService.createWeek(createWeekView, Comp.NRL);
    }

    @PutMapping("changeDeadLine/{weekNumber}")
    public OffsetDateTime changeDeadLine(@PathVariable Integer weekNumber,
                                         @Valid @RequestBody DateView dateView) {
        return adminService.changeDeadline(dateView, weekNumber, Comp.NRL);
    }

    @PostMapping("addResults")
    public BasicResponse addResults(@Valid @RequestBody ResultView resultView) {
        return adminService.addResults(resultView, Comp.NRL);
    }

    @GetMapping("gamesToUpdate")
    public List<GameDTO> getGamesToUpdate() {
        return adminService.getGamesToUpdateResult(Comp.NRL);
    }

    @PutMapping("updateTotalScore")
    public BasicResponse updateTotalScore() {
        return adminService.updateTotalScore(Comp.NRL);
    }

    @GetMapping("getAllTeams")
    public List<String> getAllTeams() {
        return adminService.getAllTeamsByComp(Comp.NRL);
    }

}
