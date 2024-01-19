package p.lodz.pl.pas2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import p.lodz.pl.pas2.Dto.UserDto.LoginDto;
import p.lodz.pl.pas2.request.LoginRequest;
import p.lodz.pl.pas2.Dto.UserDto.UserDto;
import p.lodz.pl.pas2.security.UserAuthProvider;
import p.lodz.pl.pas2.services.AuthenticationService;

@RestController
@RequestMapping("api/v1/authentication")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserAuthProvider userAuthProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginDto> loginUser(@RequestBody LoginRequest user) {
        LoginDto authUserDto = authenticationService.loginUser(user);
        authUserDto.setToken(userAuthProvider.createToken(authUserDto.getId(), authUserDto.getUserType()));
        return ResponseEntity.status(HttpStatus.OK).body(authUserDto);
    }

}
