package com.kakuom.finaltipping.controllers;

import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.responses.BasicResponse;
import com.kakuom.finaltipping.responses.GamesForWeek;
import com.kakuom.finaltipping.responses.PicksForWeek;
import com.kakuom.finaltipping.responses.ResultsForWeek;
import com.kakuom.finaltipping.services.UserService;
import com.kakuom.finaltipping.views.PickView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/api/user/")
@CrossOrigin
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{competition}/getGamesForWeek/{weekNumber}")
    public GamesForWeek getGames(@PathVariable Integer weekNumber,
                                 @PathVariable String competition) {
        return userService.getGamesForWeek(weekNumber, this.getComp(competition));
    }

    @GetMapping("{competition}/getLatestGames")
    public GamesForWeek getLatestGames(@PathVariable String competition) {
        return userService.getLatestGames(this.getComp(competition));
    }

    @PostMapping("{competition}/createPick")
    public ResponseEntity<BasicResponse> createPick(@Valid @RequestBody PickView pickView,
                                                    @PathVariable String competition) {
        return ResponseEntity.ok(userService.createPick(pickView, this.getComp(competition)));
    }

    @GetMapping("{competition}/getResultsForWeek")
    public ResultsForWeek getResultsForWeek( @PathVariable String competition,
                                             @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(required = false) String name,
                                            @RequestParam(required = false) Set<Long> gid,
                                            @RequestParam(defaultValue = "ts,desc") String[] sort
    ) {
        return userService.getResultsForWeek(this.getComp(competition), gid, name, page, size, sort);
    }

    @GetMapping("{competition}/getPicks")
    public PicksForWeek getPicksForLatestWeek(@PathVariable String competition,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) Set<Long> gid) {
        return userService.getPicksForLatestWeek(this.getComp(competition), gid, name, page, size);
    }

    @GetMapping("{competition}/getPicks/{weekNumber}")
    public PicksForWeek getPicksForWeekNumber(
            @PathVariable String competition,
            @PathVariable("weekNumber") Integer weekNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Set<Long> gid
    ) {
        return userService.getPicksForWeekNumber(weekNumber, this.getComp(competition), gid, name, page, size);
    }

    private Comp getComp(String competition) {
      return competition.equals("nrl") ? Comp.NRL : Comp.AFL;
    }
}


