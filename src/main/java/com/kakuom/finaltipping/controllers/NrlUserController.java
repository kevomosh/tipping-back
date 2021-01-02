package com.kakuom.finaltipping.controllers;

import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.responses.BasicResponse;
import com.kakuom.finaltipping.responses.GamesForWeek;
import com.kakuom.finaltipping.responses.PicksForWeek;
import com.kakuom.finaltipping.responses.ResultsForWeek;
import com.kakuom.finaltipping.security.UserPrincipal;
import com.kakuom.finaltipping.services.UserService;
import com.kakuom.finaltipping.views.PickView;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/nrl/user/")
@CrossOrigin
public class NrlUserController {
    private final UserService userService;

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
    public ResponseEntity<BasicResponse> createPick(@Valid @RequestBody PickView pickView) {
        return ResponseEntity.ok(userService.createPick(pickView, Comp.NRL));
    }

    @GetMapping("getPicks/{weekNumber}")
    public PicksForWeek getPicks(
            @PathVariable("weekNumber") Integer weekNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Set<Long> gid
    ) {
        return userService.getPicksForWeekNumber(weekNumber, Comp.NRL, gid, name, page, size);
    }

    @GetMapping("getResultsForWeek")
    public ResultsForWeek getResultsForWeek(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(required = false) String name,
                                            @RequestParam(required = false) Set<Long> gid,
                                            @RequestParam(defaultValue = "ts,desc") String[] sort
                                            ) {
        return userService.getResultsForWeek(Comp.NRL, gid, name, page, size, sort);
    }

    @GetMapping("test/{f}/{l}")
    public Object x(@PathVariable String f, @PathVariable String l){
        return StringUtils.capitalize(f) + StringUtils.capitalize(l);
    }



}
