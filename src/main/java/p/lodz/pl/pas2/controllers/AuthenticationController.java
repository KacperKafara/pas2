package p.lodz.pl.pas2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import p.lodz.pl.pas2.Dto.LoginDto;
import p.lodz.pl.pas2.Dto.UserDto;
import p.lodz.pl.pas2.model.Administrator;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.model.Moderator;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.security.UserAuthProvider;
import p.lodz.pl.pas2.services.AuthenticationService;

@RestController
@RequestMapping("api/v1/authentication")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserAuthProvider userAuthProvider;

    @PostMapping("/login")
    public ResponseEntity<UserDto> loginUser(@RequestBody LoginDto user) {
        UserDto authUserDto = authenticationService.loginUser(user);
        authUserDto.setToken(userAuthProvider.createToken(authUserDto.getId(), authUserDto.getUserType()));
        return ResponseEntity.status(HttpStatus.OK).body(authUserDto);
    }

}
