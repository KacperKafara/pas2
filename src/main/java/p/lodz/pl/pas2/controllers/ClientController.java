package p.lodz.pl.pas2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.services.ClientService;

@RestController
@RequestMapping("api/v1/clients")
public class ClientController {


    @Autowired
    private ClientService clientService;


//    @GetMapping("/get")
//    public ResponseEntity<List<ClientDTO>> allClients(){
//        return new ResponseEntity<List<ClientDTO>>(clientService.allClients(), HttpStatus.OK);
//    }
//    @PostMapping("/add")
//    public ResponseEntity<ClientDTO> addClient(@RequestBody Client client){
//
//        return new ResponseEntity<ClientDTO>(clientService.createClient(client.getFName(), client.getLName(), client.getClientType()), HttpStatus.CREATED);
//    }
}