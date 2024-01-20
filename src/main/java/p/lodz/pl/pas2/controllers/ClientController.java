package p.lodz.pl.pas2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.Dto.UserDto.UserDto;
import p.lodz.pl.pas2.Dto.UserDto.UserDtoMapper;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.request.ClientRequest;
import p.lodz.pl.pas2.services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/clients")
public class ClientController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserDtoMapper userDtoMapper;

    @Autowired
    public ClientController(UserService userService, PasswordEncoder passwordEncoder, UserDtoMapper userDtoMapper) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userDtoMapper = userDtoMapper;
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody ClientRequest user) {
        User addedUser = userService.addUser(new Client(user.getUsername(), user.isActive(), user.getFirstName(), user.getLastName(), passwordEncoder.encode(user.getPassword())));
        return ResponseEntity.status(HttpStatus.CREATED).body(userDtoMapper.clientToUserDto((Client) addedUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id, @Valid @RequestBody ClientRequest user, @RequestHeader("If-Match") String ifMatch) {
        User updatedUser = userService.updateClient(id,
                new Client(user.getUsername(), user.isActive(), user.getFirstName(), user.getLastName(), passwordEncoder.encode(user.getPassword())),
                ifMatch);
        return ResponseEntity.status(HttpStatus.OK).body(userDtoMapper.clientToUserDto((Client) updatedUser));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getClients() {
        return ResponseEntity.status(HttpStatus.OK).body(userDtoMapper.clientsToUserDtos(userService.getClients()));
    }
}