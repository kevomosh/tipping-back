package com.kakuom.finaltipping.controllers;

import com.kakuom.finaltipping.dto.GameDTO;
import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.responses.BasicResponse;
import com.kakuom.finaltipping.services.AdminService;
import com.kakuom.finaltipping.views.*;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/afl/admin/")
@CrossOrigin
public class AflAdminController {
    private AdminService adminService;

    public AflAdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("createGroups")
    public BasicResponse createGroups(@RequestBody StringSetView view) {
        return adminService.createGroups(view, Comp.AFL);
    }

    @PostMapping("createTeams")
    public BasicResponse createTeams(
            @RequestBody StringSetView view
    ) {
        return adminService.createTeams(view, Comp.AFL);
    }

    @PostMapping("addPlayersToTeams")
    public BasicResponse addPlayers(
            @RequestBody List<AddPlayersView> addPlayersViewList
    ) {
        return adminService.addPlayersToTeam(addPlayersViewList, Comp.AFL);
    }

    @PostMapping("createWeek")
    public BasicResponse createWeek(@RequestBody CreateWeekView createWeekView) {
        return adminService.createWeek(createWeekView, Comp.AFL);
    }

    @PutMapping("changeDeadLine/{weekNumber}")
    public OffsetDateTime changeDeadLine(@PathVariable Integer weekNumber,
                                         @RequestBody DateView dateView) {
        return adminService.changeDeadline(dateView, weekNumber, Comp.AFL);
    }

    @PostMapping("addResults/{weekNumber}")
    public BasicResponse addResults(@PathVariable Integer weekNumber,
                                    @RequestBody ResultView resultView) {
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
