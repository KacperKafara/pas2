package p.lodz.pl.pas2.repositories.Implementations;

import org.springframework.stereotype.Repository;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.repositories.ClientRepository;

import java.util.List;
import java.util.UUID;

@Repository
public class ClientRepositoryImplementation implements ClientRepository {
    @Override
    public Client findClient(UUID id) {
        return null;
    }

    @Override
    public Client findClient(String nickname) {
        return null;
    }

    @Override
    public List<Client> findClientsMatchToValue(String nickname) {
        return null;
    }

    @Override
    public Client saveClient(Client client) {
        return null;
    }

    @Override
    public List<Client> findClients() {
        return null;
    }

    @Override
    public Client setActive(UUID id, boolean active) {
        return null;
    }

    @Override
    public Client changeUsername(UUID id, String username) {
        return null;
    }
}
