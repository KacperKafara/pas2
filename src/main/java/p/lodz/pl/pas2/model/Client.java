package p.lodz.pl.pas2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Client {
    private UUID id;
    private String username;
    private ClientType clientType;
    private boolean active;

    public Client(String username, ClientType clientType, boolean active) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.clientType = clientType;
        this.active = active;
    }
}
