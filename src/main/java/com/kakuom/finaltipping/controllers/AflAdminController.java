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
@RequestMapping("/api/afl/admin/")
@CrossOrigin
@PreAuthorize("hasRole('ADMIN')")
public class AflAdminController {
    private final AdminService adminService;

    public AflAdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("createGroups")
    public BasicResponse createGroups(@Valid @RequestBody StringSetView view) {
        return adminService.createGroups(view, Comp.AFL);
    }

    @PostMapping("createTeams")
    public BasicResponse createTeams(
            @Valid @RequestBody StringSetView view
    ) {
        return adminService.createTeams(view, Comp.AFL);
    }

    @PostMapping("addPlayersToTeams")
    public BasicResponse addPlayers(
            @Valid @RequestBody AddPlayersView addPlayersView
    ) {
        return adminService.addPlayersToTeam(addPlayersView, Comp.AFL);
    }

    @PostMapping("createWeek")
    public BasicResponse createWeek(@Valid @RequestBody CreateWeekView createWeekView) {
        return adminService.createWeek(createWeekView, Comp.AFL);
    }

    @PutMapping("changeDeadLine/{weekNumber}")
    public OffsetDateTime changeDeadLine(@PathVariable Integer weekNumber,
                                         @Valid @RequestBody DateView dateView) {
        return adminService.changeDeadline(dateView, weekNumber, Comp.AFL);
    }

    @PostMapping("addResults/{weekNumber}")
    public BasicResponse addResults(@PathVariable Integer weekNumber,
                                    @Valid @RequestBody ResultView resultView) {
        return adminService.addResults(weekNumber, resultView, Comp.AFL);
    }

    @GetMapping("gamesToUpdate/{weekNumber}")
    public List<GameDTO> getGamesToUpdate(
            @PathVariable Integer weekNumber
    ) {
        return adminService.getGamesToUpdateResult(weekNumber, Comp.AFL);
    }

    @PutMapping("updateTotalScore/{weekNumber}")
    public BasicResponse updateTotalScore(@PathVariable Integer weekNumber) {
        return adminService.updateTotalScore(weekNumber, Comp.AFL);
    }

    @GetMapping("getAllTeams")
    public List<String> getAllTeams() {
        return adminService.getAllTeamsByComp(Comp.AFL);
    }
}
