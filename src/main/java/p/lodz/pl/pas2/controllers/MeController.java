package p.lodz.pl.pas2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.security.UserAuthProvider;
import p.lodz.pl.pas2.services.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/me")
public class MeController {

    private final UserService userService;
    private final UserAuthProvider userAuthProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MeController(UserService userService, UserAuthProvider userAuthProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userAuthProvider = userAuthProvider;
        this.passwordEncoder = passwordEncoder;
    }

    private User getUser(String complexToken) {
        String token = complexToken.replace("Bearer ", "");
        return userService.getUser(userAuthProvider.getUserId(token));
    }

    @PatchMapping("/password")
    public String changePassword(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> body) {
        User user = getUser(token);
        String newPassword = body.get("password");
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateUser(user.getId(), user);
        return "Password changed";
    }
}
