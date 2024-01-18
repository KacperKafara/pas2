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
  private AuthenticationService authenticationService;
  private UserAuthProvider userAuthProvider;

     @PostMapping("registerClient")
    public ResponseEntity<UserDto>registerClient(@RequestBody Client user){
         return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.registerClient(user));
     }
    @PostMapping("registerAdmin")
    public ResponseEntity<UserDto>registerAdmin(@RequestBody Administrator user){
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.registerAdmin(user));
    }
    @PostMapping("registerModerator")
    public ResponseEntity<UserDto>registerModerator(@RequestBody Moderator user){
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.registerModerator(user));
    }
     @PostMapping("login")
     public ResponseEntity<UserDto>loginUser(@RequestBody LoginDto user) {
            UserDto authUserDto = authenticationService.loginUser(user);
            authUserDto.setToken(userAuthProvider.createToken(authUserDto.getLogin()));
      return ResponseEntity.status(HttpStatus.CREATED).body(authUserDto);
     }

}
