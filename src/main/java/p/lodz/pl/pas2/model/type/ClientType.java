package p.lodz.pl.pas2.model.type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor
@BsonDiscriminator(key = "_clazz")
public abstract class ClientType {

    @BsonProperty("_id")
    protected ObjectId id;

    @BsonProperty("clientdiscount")
    protected double clientDiscount;

    @BsonProperty("shorterdeliverytime")
    protected int shorterDeliveryTime;

    @BsonCreator
    public ClientType(@BsonProperty("clientdiscount") double clientDiscount,
                      @BsonProperty("shorterdeliverytime") int shorterDeliveryTime) {
        this.id = new ObjectId();
        this.clientDiscount = clientDiscount;
        this.shorterDeliveryTime = shorterDeliveryTime;
    }

}
