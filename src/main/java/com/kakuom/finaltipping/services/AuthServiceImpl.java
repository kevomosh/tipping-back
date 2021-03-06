package com.kakuom.finaltipping.services;

import com.kakuom.finaltipping.enums.Comp;
import com.kakuom.finaltipping.enums.Role;
import com.kakuom.finaltipping.model.Groups;
import com.kakuom.finaltipping.model.PassToken;
import com.kakuom.finaltipping.model.User;
import com.kakuom.finaltipping.repositories.GroupRepository;
import com.kakuom.finaltipping.repositories.PassTokenRepository;
import com.kakuom.finaltipping.repositories.UserRepository;
import com.kakuom.finaltipping.responses.BasicResponse;
import com.kakuom.finaltipping.responses.JwtResponse;
import com.kakuom.finaltipping.security.JwtProvider;
import com.kakuom.finaltipping.security.UserPrincipal;
import com.kakuom.finaltipping.views.LoginView;
import com.kakuom.finaltipping.views.RegisterView;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder encoder;
    private JwtProvider jwtProvider;
    private GroupRepository groupRepository;
    private PassTokenRepository passTokenRepository;
    private JavaMailSender mailSender;

    public AuthServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager,
                           PasswordEncoder encoder, JwtProvider jwtProvider,
                           GroupRepository groupRepository, PassTokenRepository passTokenRepository,
                           JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtProvider = jwtProvider;
        this.groupRepository = groupRepository;
        this.passTokenRepository = passTokenRepository;
        this.mailSender = mailSender;
    }

    @Override
    public BasicResponse registerUser(RegisterView registerView) {
        if (!validEmail(registerView.getEmail().toLowerCase(Locale.ENGLISH))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter valid email");
        }
        if (!passwordIsValid(registerView.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter valid password");
        }
        if (userRepository.existsByEmail(registerView.getEmail().toLowerCase(Locale.ENGLISH))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        if (userRepository.existsByName(registerView.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nickname already exists");

        }

        User newUser = new User(StringUtils.capitalize(registerView.getName()),
                registerView.getEmail().toLowerCase(Locale.ENGLISH),
                encoder.encode(registerView.getPassword()), Role.USER);


        var groupIds = registerView.getGroupIds();
        if (groupIds != null && !groupIds.isEmpty()) {
            groupIds
                    .stream()
                    .map(groupId -> groupRepository.findById(groupId))
                    .filter(Optional::isPresent)
                    .limit(6)
                    .map(Optional::get)
                    .forEach(newUser::addGroup);
        }
        userRepository.save(newUser);

        return new BasicResponse("User registered");
    }

    @Override
    public JwtResponse loginUser(LoginView loginView) {
        if (!passwordIsValid(loginView.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter valid password");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginView.getEmail().toLowerCase(Locale.ENGLISH),
                        loginView.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();

        var nrlGroups = groupRepository.getGroupsForUser(userDetails.getId(), Comp.NRL);
        var aflGroups = groupRepository.getGroupsForUser(userDetails.getId(), Comp.AFL);

        return new JwtResponse(jwt, userDetails.getUsername(), nrlGroups, aflGroups);

    }

    @Override
    public Map<String, Object> getAllGroupsByComp() {
        Map<String, Object> response = new HashMap<>();
        response.put("aflGroups", groupRepository.getAllGroupsByComp(Comp.AFL));
        response.put("nrlGroups", groupRepository.getAllGroupsByComp(Comp.NRL));
        return response;
    }

    // TODO FORGOT PASSWORD STILL CASE SENSITIVE
    @Override
    public BasicResponse createPasswordToken(String email) {
        if (!validEmail(email.toLowerCase(Locale.ENGLISH))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please enter valid email");
        }


        var user = userRepository.findByEmail(email.toLowerCase(Locale.ENGLISH))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesnt exist"));

        passTokenRepository.getTokenByUserId(user.getId())
                .ifPresentOrElse(passToken -> {
                    passToken.setExpiry(OffsetDateTime.now().plusDays(1));
                    passTokenRepository.save(passToken);
                    sendEmail(user.getEmail(), passToken.getToken());
                }, () -> {
                    var token = new PassToken(UUID.randomUUID(), OffsetDateTime.now().plusDays(1));
                    user.setPassToken(token);
                    userRepository.save(user);
                    sendEmail(user.getEmail(), token.getToken());

                });

        return new BasicResponse("Message sent to your email address");

    }

    @Override
    public BasicResponse changePassword(String password, String token) {
        if (!passwordIsValid(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter valid password");
        }
        try {
            UUID uuid = UUID.fromString(token);
            var user = passTokenRepository.getUserByToken(uuid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token, " +
                            "Click on forgot password button."));


            if (OffsetDateTime.now().isAfter(user.getPassToken().getExpiry())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token has expired," +
                        " please go to change password page");
            }

            user.setPassword(encoder.encode(password));
            user.setPassToken(null);
            userRepository.save(user);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token, " +
                    "Click on forgot password button.");
        }

        return new BasicResponse("Password changed successfully. You can now Log in with the new password");
    }

    @Override
    public BasicResponse createDefaultGroup() {
        var g = new Groups("Party House", Comp.NRL);
        var g2 = new Groups("Misc", Comp.NRL);
        var g3 = new Groups("Misc", Comp.AFL);


        groupRepository.save(g);
        groupRepository.save(g2);
        groupRepository.save(g3);

        return new BasicResponse("Created, now disable it");
    }

    @Async
    public void sendEmail(String email, UUID token) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Change Password for Tipping Comp");
        msg.setText("Please click on the link below to reset your password. \n" +
                "The link will expire in 24 hrs \n" +
                "https://tipping.kakuom.com/auth/reset-password?token=" + token);
        msg.setFrom("admin@kakuom.com");
        mailSender.send(msg);
    }

    private boolean validEmail(String email) {
        Predicate<String> IS_EMAIL_VALID =
                Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
                        .asPredicate();
        return IS_EMAIL_VALID.test(email);
    }

    private boolean passwordIsValid(String password) {
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
        return password.matches(pattern);
    }
}
