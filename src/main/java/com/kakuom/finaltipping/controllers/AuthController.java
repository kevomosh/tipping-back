package com.kakuom.finaltipping.controllers;

import com.kakuom.finaltipping.responses.BasicResponse;
import com.kakuom.finaltipping.responses.JwtResponse;
import com.kakuom.finaltipping.services.AuthService;
import com.kakuom.finaltipping.views.CreateTokenView;
import com.kakuom.finaltipping.views.LoginView;
import com.kakuom.finaltipping.views.PasswordView;
import com.kakuom.finaltipping.views.RegisterView;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
@CrossOrigin
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public BasicResponse registerUser(@Valid @RequestBody RegisterView registerView) {
        return authService.registerUser(registerView);
    }

    @PostMapping("login")
    public JwtResponse login(@Valid @RequestBody LoginView loginView) {
        return authService.loginUser(loginView);
    }

    @GetMapping("getGroups")
    public Map<String, Object> getAllGroups() {
        return authService.getAllGroupsByComp();
    }

    @PostMapping("createToken")
    public BasicResponse createToken(@Valid @RequestBody CreateTokenView view) {
        return authService.createPasswordToken(view.getEmail());
    }

    @PostMapping("changePassword/{token}")
    public BasicResponse changePassword(@Valid @RequestBody PasswordView view,
                                        @PathVariable String token) {

        return authService.changePassword(view.getNewPassword().strip(), token);
    }

//    @PostMapping("createDefaultGroup")
//    public BasicResponse createDefaultGroup(){
//
//        return authService.createDefaultGroup();
//    }


}
