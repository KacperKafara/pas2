package p.lodz.pl.pas2.model;

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
public class User {
    @BsonId
    private UUID id;
    @BsonProperty("username")
    private String username;
    @BsonProperty("user_type")
    private UserType userType;
    @BsonProperty("active")
    private boolean active;


    @BsonCreator
    public User(@BsonId UUID id,
                @BsonProperty("username") String username,
                @BsonProperty("userType") UserType userType,
                @BsonProperty("active") boolean active) {
        this.username = username;
        this.userType = userType;
        this.active = active;
    }


    public User(String username, UserType userType, boolean active) {
        this.username = username;
        this.userType = userType;
        this.active = active;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
