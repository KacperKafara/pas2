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

import java.util.List;

@Configuration
@Profile("test")
public class TestMongoConfig  {
    MongoDatabase database;
    TestMongoConfig(){
        initDbConnection();
    }
    protected void initDbConnection() {
        ConnectionString testConnectionString = new ConnectionString(
                "mongodb://localhost:27017,localhost:27018,localhost:27019/test_pas?replicaSet=replica_set_test"
        );
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder()
                .automatic(true)
                .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                .build());
        MongoCredential testCredential = MongoCredential.createCredential("admin",
                "adminpassword", "adminpassword".toCharArray());

        MongoClientSettings testSettings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyConnectionString(testConnectionString)
                .credential(testCredential)
                .codecRegistry(CodecRegistries.fromRegistries(
                        CodecRegistries.fromProviders(new CustomCodecProvider()),
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                ))
                .build();

        MongoClient mongoClient = MongoClients.create(testSettings);
          database = mongoClient.getDatabase("test_pas");
    }
    /*@Bean
    MongoDatabase getDatabase() {
        return database;
    } */
}
