package p.lodz.pl.pas2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.request.ClientRequest;
import p.lodz.pl.pas2.services.UserService;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/clients")
public class ClientController {
    private final UserService userService;

    @Autowired
    public ClientController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody ClientRequest user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(new Client(user.getUsername(), user.isActive(), user.getFirstName(), user.getLastName())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @Valid @RequestBody ClientRequest user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, new Client(user.getUsername(), user.isActive(), user.getFirstName(), user.getLastName())));
    }
}