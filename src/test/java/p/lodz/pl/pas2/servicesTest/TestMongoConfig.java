package p.lodz.pl.pas2.servicesTest;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import p.lodz.pl.pas2.CustomCodecProvider;
import p.lodz.pl.pas2.repositories.AbstractMongoRepositoryConfig;

import java.io.Closeable;
import java.util.List;

@Configuration
@Profile("test")
public class TestMongoConfig implements Closeable {
    private MongoClient mongoClient;
    private MongoDatabase database;

    ConnectionString connectionString = new ConnectionString(
            "mongodb://localhost:27017/pas?replicaSet=replica_set_single"
    );
    MongoCredential credential = MongoCredential.createCredential("admin",
            "admin", "adminpassword".toCharArray());
    CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder()
            .automatic(true)
            .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
            .build());

    @Bean
    public MongoDatabase getdataBase(){
        return database;
    }

    private void initDbConnection() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyConnectionString(connectionString)
                .credential(credential)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        CodecRegistries.fromProviders(new CustomCodecProvider()),
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                ))
                .build();

        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("pas");
    }

    public TestMongoConfig() {
        initDbConnection();
    }

    @Override
    public void close() {
        database.drop();
        mongoClient.close();
    }
}
