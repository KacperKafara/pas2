package p.lodz.pl.pas2;

import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import p.lodz.pl.pas2.model.Administrator;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.model.Moderator;
import p.lodz.pl.pas2.model.User;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UserTypeCodec implements Codec<User> {
    private final CodecRegistry registry;
    private Codec<UUID> uuidCodec;
    public UserTypeCodec(CodecRegistry registry) {
        this.registry = registry;
    }

    @Override
    public User decode(BsonReader bsonReader, DecoderContext decoderContext) {
        bsonReader.readStartDocument();
        UUID id = null;
        String username = null;
        boolean active = false;
        String firstName = null;
        String lastName = null;
        String _clazz = null;
        while (bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String fieldName = bsonReader.readName();
            switch (fieldName) {
                case "_id":
                    id = bsonReader.readBinaryData().asUuid(UuidRepresentation.STANDARD);
//                    id = UUID.fromString(bsonReader.readString());
                    break;
                case "username":
                    username = bsonReader.readString();
                    break;
                case "active":
                    active = bsonReader.readBoolean();
                    break;
                case "firstname":
                    firstName = bsonReader.readString();
                    break;
                case "lastname":
                    lastName = bsonReader.readString();
                    break;
                case "_clazz":
                    _clazz = bsonReader.readString();
                    break;
                default:
                    bsonReader.skipValue();
            }
        }
        bsonReader.readEndDocument();
        if(_clazz != null && _clazz.equals("client")) {
            return new Client(id, username, active, firstName, lastName);
        } else if (_clazz != null && _clazz.equals("administrator")) {
            return new Administrator(id, username, active);
        } else {
            return new Moderator(id, username, active);
        }
    }

    @Override
    public void encode(BsonWriter bsonWriter, User user, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        bsonWriter.writeString("_clazz", user.getClass().getSimpleName().toLowerCase());
        bsonWriter.writeBinaryData("_id", new BsonBinary(user.getId()));
//        bsonWriter.writeString("_id", user.getId().toString());
        bsonWriter.writeString("username", user.getUsername());
        bsonWriter.writeBoolean("active", user.isActive());
        if (user instanceof Client) {
            bsonWriter.writeName("firstname");
            bsonWriter.writeString(((Client) user).getFirstName());
            bsonWriter.writeName("lastname");
            bsonWriter.writeString(((Client) user).getLastName());
        }
        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<User> getEncoderClass() {
        return User.class;
    }
}
