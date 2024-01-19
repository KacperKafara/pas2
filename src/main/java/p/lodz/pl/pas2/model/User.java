package p.lodz.pl.pas2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public abstract class User {
    @BsonId
    protected UUID id;

    @BsonProperty("username")
    protected String username;

    @BsonProperty("active")
    protected boolean active;

    @BsonProperty("password")
    protected String password;

    @BsonCreator
    public User(@BsonId UUID id,
                @BsonProperty("username") String username,
                @BsonProperty("active") boolean active,
                @BsonProperty("password") String password) {
        this.id = id;
        this.username = username;
        this.active = active;
        this.password = password;
    }


    public User(String username, boolean active, String password) {
        this.username = username;
        this.active = active;
        this.password = password;
    }
}
