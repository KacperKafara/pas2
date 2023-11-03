package p.lodz.pl.pas2.dto;

import org.springframework.stereotype.Service;
import p.lodz.pl.pas2.model.Client;

import java.util.function.Function;

@Service
public class ClientDTOMapper  implements Function<Client, ClientDTO> {
    @Override
    public ClientDTO apply(Client client) {
        return new ClientDTO(
                client.getFName(),
                client.getLName(),
                client.getClientType()
        );
    }
}
