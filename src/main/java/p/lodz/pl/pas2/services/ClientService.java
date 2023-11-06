package p.lodz.pl.pas2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import p.lodz.pl.pas2.dto.ClientDTO;
import p.lodz.pl.pas2.dto.ClientDTOMapper;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.model.Product;
import p.lodz.pl.pas2.repositories.ClientRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {


    private final ClientRepository repository;

    private final ClientDTOMapper mapper;

    @Autowired
    public ClientService(ClientRepository repository, ClientDTOMapper mapper) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public List<ClientDTO> allClients(){
        return  repository.findAll()
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    public ClientDTO createClient(String name, String lname, Client.Type clientType){
        Client client = repository.insert( new Client(name, lname, clientType));

        // Dodawanie nowego id do listy id w dokumencie dla innej klasy
//        mongoTemplate.update(Client.class)
//                .matching(Criteria.where("id").is(clientId))
//                .apply(new Update().push("productIds").value(product));
        return mapper.apply(client);
    }
}
