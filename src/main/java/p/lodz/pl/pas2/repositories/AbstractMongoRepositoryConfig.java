package p.lodz.pl.pas2.repositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.util.List;

@Configuration
@Getter
@Setter
public class AbstractMongoRepositoryConfig implements Closeable {

    //@Value("${mongodb.connection-string}")
    private String connectionString = "mongodb://localhost:27017,localhost:27018,localhost:27019/pas?replicaSet=replica_set_single";
    private String databaseName = "pas" ;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCredential credential;
    CodecRegistry pojoCodecRegistry;
    public AbstractMongoRepositoryConfig() {
        credential = MongoCredential.createCredential("admin",
                "admin", "adminpassword".toCharArray());
        pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder()
                .automatic(true)
                .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                .build());

        initDbConnection();
    }

    @Bean
    public MongoDatabase getdataBase() {
        return database;
    }
    private void initDbConnection() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .credential(credential)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        CodecRegistries.fromProviders(PojoCodecProvider.builder().build()),
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                ))
                .build();

        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase(databaseName);
    }



    @Override
    public void close() {
        database.drop();
        mongoClient.close();
    }
}
