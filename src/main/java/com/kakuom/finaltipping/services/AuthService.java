package com.kakuom.finaltipping.services;

import com.kakuom.finaltipping.responses.BasicResponse;
import com.kakuom.finaltipping.responses.JwtResponse;
import com.kakuom.finaltipping.views.LoginView;
import com.kakuom.finaltipping.views.RegisterView;

import java.util.Map;

public interface AuthService {
    BasicResponse registerUser(RegisterView registerView);

    JwtResponse loginUser(LoginView loginView);

    Map<String, Object> getAllGroupsByComp();

    BasicResponse createPasswordToken(String email);

    BasicResponse changePassword(String password, String token);

    BasicResponse createDefaultGroup(String name);

}
