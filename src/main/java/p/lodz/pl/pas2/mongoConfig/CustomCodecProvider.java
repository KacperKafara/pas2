package p.lodz.pl.pas2.mongoConfig;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.stereotype.Component;
import p.lodz.pl.pas2.model.Administrator;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.model.Moderator;
import p.lodz.pl.pas2.model.User;

@Component
public class CustomCodecProvider implements CodecProvider {
    public CustomCodecProvider() {

    }
    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == Moderator.class || clazz == Client.class || clazz == Administrator.class || clazz == User.class) {
            return (Codec<T>) new UserTypeCodec(registry);
        }
        return null;
    }
}
