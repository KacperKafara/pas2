package p.lodz.pl.pas2.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

    @NotNull
    @NotBlank
    @BsonProperty("username")
    protected String username;

    @BsonProperty("active")
    protected boolean active;

    @BsonCreator
    public User(@BsonId UUID id,
                @BsonProperty("username") String username,
                @BsonProperty("active") boolean active) {
        this.id = id;
        this.username = username;
        this.active = active;
    }

    public User(String username, boolean active) {
        this.username = username;
        this.active = active;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
