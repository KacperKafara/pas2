package p.lodz.pl.pas2.model.type;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "_clazz", value = "standard")
public class Standard extends ClientType{
    public Standard() {
        super(0, 0);
    }
}
