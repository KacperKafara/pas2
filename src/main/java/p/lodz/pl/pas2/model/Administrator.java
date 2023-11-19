package p.lodz.pl.pas2.model;

import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@NoArgsConstructor
public class Administrator extends User {
    public Administrator(String username, boolean active) {
        super(username, active);
    }
    public Administrator(UUID id,
                         String username,
                         boolean active) {
        super(id, username, active);
    }
}
