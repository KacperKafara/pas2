package p.lodz.pl.pas2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.services.ClientService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClient(@PathVariable UUID id) {
        return new ResponseEntity<>(clientService.getClient(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Client>> getClients() {
        return new ResponseEntity<>(clientService.getClients(), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Client> getClient(@PathVariable String username) {
        return new ResponseEntity<>(clientService.getClient(username), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Client> addClient(@RequestBody Client client) {
        return new ResponseEntity<>(clientService.addClient(client), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> setActive(@PathVariable UUID id, @RequestBody boolean active) {
        return new ResponseEntity<>(clientService.setActive(id, active), HttpStatus.OK);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Client> changeUsername(@PathVariable UUID id, @RequestBody String username) {
//        return new ResponseEntity<>(clientService.changeUsername(id, username), HttpStatus.OK);
//    }
}