package p.lodz.pl.pas2.model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

public class Moderator extends User {
    public Moderator(String username, boolean active) {
        super(username, active);
    }
    public Moderator(){

    }

    public Moderator(UUID id,
                     String username,
                     boolean active) {
        super(id, username, active);
    }
}
