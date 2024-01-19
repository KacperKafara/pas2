package p.lodz.pl.pas2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.Dto.UserDto.UserDto;
import p.lodz.pl.pas2.Dto.UserDto.UserDtoMapper;
import p.lodz.pl.pas2.model.Moderator;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.request.ModeratorRequest;
import p.lodz.pl.pas2.services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/moderators")
public class ModeratorController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserDtoMapper userDtoMapper;

    @Autowired
    public ModeratorController(UserService userService, PasswordEncoder passwordEncoder, UserDtoMapper userDtoMapper) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userDtoMapper = userDtoMapper;
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody ModeratorRequest user) {
        User addedUser = userService.addUser(new Moderator(user.getUsername(), user.isActive(), passwordEncoder.encode(user.getPassword())));
        return ResponseEntity.status(HttpStatus.CREATED).body(userDtoMapper.moderatorToUserDto((Moderator) addedUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id, @Valid @RequestBody ModeratorRequest user) {
        User updatedUser = userService.updateUser(id, new Moderator(user.getUsername(), user.isActive(), passwordEncoder.encode(user.getPassword())));
        return ResponseEntity.status(HttpStatus.OK).body(userDtoMapper.moderatorToUserDto((Moderator) updatedUser));
    }

    @GetMapping
    public  ResponseEntity<List<UserDto>> getModerators() {
        List<UserDto> userDtos = userDtoMapper.moderatorsToUserDtos(userService.getModerators());
        return ResponseEntity.status(HttpStatus.OK).body(userDtos);
    }
}