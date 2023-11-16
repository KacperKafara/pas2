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
import org.springframework.stereotype.Component;


import java.io.Closeable;
import java.util.List;

@Component
@Getter
public class AbstractMongoRepositoryConfig implements Closeable {
    private MongoClient mongoClient;
    private MongoDatabase database;

    ConnectionString connectionString = new ConnectionString(
            "mongodb://localhost:27017"
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
                //.credential(credential)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        CodecRegistries.fromProviders(PojoCodecProvider.builder().build()),
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                ))
                .build();

        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("pas");
    }

//    @Bean
//    public MongoClient mongoClient() {
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .applyConnectionString(connectionString)
//                .credential(credential)
//                .uuidRepresentation(UuidRepresentation.STANDARD)
//                .codecRegistry(CodecRegistries.fromRegistries(
//                        CodecRegistries.fromProviders(PojoCodecProvider.builder().build()),
//                        MongoClientSettings.getDefaultCodecRegistry(),
//                        pojoCodecRegistry
//                ))
//                .build();
//        mongoClient = MongoClients.create(settings);
//        return mongoClient;
//    }

    public AbstractMongoRepositoryConfig() {
        initDbConnection();
    }

    @Override
    public void close() {
        database.drop();
        mongoClient.close();
    }
}