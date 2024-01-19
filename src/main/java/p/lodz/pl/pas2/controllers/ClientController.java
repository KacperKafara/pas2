package p.lodz.pl.pas2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    public ClientController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody ClientRequest user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(new Client(user.getUsername(), user.isActive(), user.getFirstName(), user.getLastName(), passwordEncoder.encode(user.getPassword()))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @Valid @RequestBody ClientRequest user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateClient(id, new Client(user.getUsername(), user.isActive(), user.getFirstName(), user.getLastName(), passwordEncoder.encode(user.getPassword()))));
    }

    @GetMapping
    public ResponseEntity<List<User>> getClients() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getClients());
    }
}