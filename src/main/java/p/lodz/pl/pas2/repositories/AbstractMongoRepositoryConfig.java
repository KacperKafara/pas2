package p.lodz.pl.pas2.repositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.List;

@Configuration
@Getter
public class AbstractMongoRepositoryConfig {
    private MongoClient mongoClient;
    private MongoDatabase database;
    ConnectionString connectionString = new ConnectionString(
            "mongodb://localhost:27017,localhost:27018,localhost:27019/pas?replicaSet=replica_set_single"
    );
    MongoCredential credential = MongoCredential.createCredential("admin",
            "admin", "adminpassword".toCharArray());
    CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder()
            .automatic(true)
            .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
            .build());


    private void initDbConnection() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .credential(credential)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        CodecRegistries.fromProviders(PojoCodecProvider.builder().build()),
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                ))
                .build();

        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("online-shop");
    }

    @Bean
    public MongoClient mongoClient() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .credential(credential)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        CodecRegistries.fromProviders(PojoCodecProvider.builder().build()),
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                ))
                .build();
        return MongoClients.create(settings);
    }

    public AbstractMongoRepositoryConfig() {
        initDbConnection();
    }
}
