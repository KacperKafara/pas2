package p.lodz.pl.pas2.repositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;


import java.util.List;

//@Component
@Configuration
@Service
public class AbstractMongoRepositoryConfig {
//
//    private MongoClient mongoClient;
//    private MongoDatabase database;


//    private void initDbConnection() {
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
//
//        mongoClient = MongoClients.create(settings);
//        database = mongoClient.getDatabase("online-shop");
//    }

    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(
                "mongodb://localhost:27017,localhost:27018,localhost:27019/online-shop?replicaSet=replica_set_single"
        );
        MongoCredential credential = MongoCredential.createCredential("admin",
                "admin", "adminpassword".toCharArray());
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder()
                .automatic(true)
                .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                .build());

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

//    public AbstractMongoRepositoryConfig() {
//    }

//    @Override
//    public void close() {
//        database.drop();
//        mongoClient.close();
//    }
}
