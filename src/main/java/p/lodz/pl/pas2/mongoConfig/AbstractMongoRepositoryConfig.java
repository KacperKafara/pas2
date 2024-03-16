package p.lodz.pl.pas2.mongoConfig;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.ValidationOptions;
import lombok.Getter;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import p.lodz.pl.pas2.mongoConfig.CustomCodecProvider;
import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.model.User;


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

    @Bean
    public MongoDatabase getDataBase(){
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

    @Bean
    public FindOneAndUpdateOptions findOneAndUpdateOptions() {
        return new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
    }

    @Bean
    public MongoCollection<Movie> createMovieCollection() {
        Document validator = Document.parse("""
                    {
                        $jsonSchema:{
                            "bsonType": "object",
                            "required": ["title", "cost"],
                            "properties": {
                                "title": {
                                    "bsonType": "string",
                                    "minLength": 1
                                },
                                "cost": {
                                    "bsonType": "double",
                                    "minimum": 0
                                }
                            }
                        }
                    }
                """);
        try {
            ValidationOptions validationOptions = new ValidationOptions().validator(validator);
            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions().validationOptions(validationOptions);
            database.createCollection("movies", createCollectionOptions);
            return database.getCollection("movies", Movie.class);
        } catch (MongoCommandException ignored) {
            Document command = new Document("collMod", "movies").append("validator", validator);
            database.runCommand(command);
            return database.getCollection("movies", Movie.class);
        }
    }

    @Bean
    public MongoCollection<User> createUserCollection() {
        Document validator = Document.parse("""
                    {
                        $jsonSchema: {
                            "bsonType": "object",
                            "required": ["username", "active"],
                            "properties": {
                                "username": {
                                    "bsonType": "string",
                                    "minLength": 1
                                },
                                "active": {
                                    "bsonType": "bool"
                                },
                                "firstname": {
                                    "bsonType": "string",
                                    "minLength": 1
                                },
                                "lastName": {
                                    "bsonType": "string",
                                    "minLength": 1
                                }
                            }
                        }
                    }
                """);
        try {
            ValidationOptions validationOptions = new ValidationOptions().validator(validator);
            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions().validationOptions(validationOptions);
            database.createCollection("users", createCollectionOptions);
            return database.getCollection("users", User.class);
        } catch (MongoCommandException ignored) {
            Document command = new Document("collMod", "users").append("validator", validator);
            database.runCommand(command);
            return database.getCollection("users", User.class);
        }
    }

    public AbstractMongoRepositoryConfig() {
        initDbConnection();
    }

    @Override
    public void close() {
        database.drop();
        mongoClient.close();
    }
}
