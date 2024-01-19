package p.lodz.pl.pas2.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Client extends User {

    @BsonProperty("firstname")
    private String firstName;

    @BsonProperty("lastname")
    private String lastName;


    public Client(String username, boolean active, String firstName, String lastName, String password) {
        super(username, active, password);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Client(UUID id,
                  String username,
                  boolean active,
                  String firstName,
                  String lastName,
                  String password) {
        super(id, username, active, password);
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
