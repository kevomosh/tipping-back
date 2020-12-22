package com.kakuom.finaltipping.controllers;

import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.responses.BasicResponse;
import com.kakuom.finaltipping.responses.GamesForWeek;
import com.kakuom.finaltipping.responses.ResultsForWeek;
import com.kakuom.finaltipping.services.UserService;
import com.kakuom.finaltipping.views.PickView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/nrl/user/")
@CrossOrigin
public class NrlUserController {
    private UserService userService;

    public NrlUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("getGamesForWeek/{weekNumber}")
    public GamesForWeek getGames(@PathVariable Integer weekNumber) {
        return userService.getGamesForWeek(weekNumber, Comp.NRL);
    }

    @GetMapping("getLatestGames")
    public GamesForWeek getLatestGames() {
        return userService.getLatestGames(Comp.NRL);
    }

    @PostMapping("createPick")
    public ResponseEntity<BasicResponse> createPick(@RequestBody PickView pickView) {
        return ResponseEntity.ok(userService.createPick(pickView, Comp.NRL));
    }

    @GetMapping("getPicks/{userId}/{weekNumber}")
    public Map<String, Object> getPicks(
            @PathVariable("userId") Long userId,
            @PathVariable("weekNumber") Integer weekNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Set<Long> gid
    ) {
        return userService.getPicksForWeekNumber(userId, weekNumber, Comp.NRL, gid, name, page, size);
    }

    @GetMapping("getResultsForWeek/{userId}")
    public ResultsForWeek getResultsForWeek(@PathVariable Long userId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(required = false) String name,
                                            @RequestParam(required = false) Set<Long> gid) {
        return userService.getResultsForWeek(Comp.NRL, userId, gid, name, page, size);
    }
}
