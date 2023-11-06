package p.lodz.pl.pas2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.services.ClientService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> getClientById(@PathVariable UUID id) {
        return new ResponseEntity<>(clientService.getClient(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> getClients() {
        return new ResponseEntity<>(clientService.getClients(), HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getClientByNickname(@PathVariable String username) {
        return new ResponseEntity<>(clientService.getClient(username), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> addClient(@RequestBody User user) {
        User addedUser = clientService.addClient(user);
        if(addedUser == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(addedUser, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> setActive(@PathVariable UUID id, @RequestBody Map<String, Boolean> active) {
        User updatedUser = clientService.setActive(id, Boolean.parseBoolean(active.get("active").toString()));
        if(updatedUser == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateClient(@PathVariable UUID id, @RequestBody User user) {
        User updatedUser = clientService.updateClient(id, user);
        if(updatedUser == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}