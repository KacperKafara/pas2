package p.lodz.pl.pas2.repositories.Implementations.mongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserRepositoryMongoDB implements UserRepository {
    private final MongoCollection<User> userMongoCollection;

//    @Autowired
    public UserRepositoryMongoDB(MongoClient mongoClient) {
        this.userMongoCollection = mongoClient.getDatabase("pas").getCollection("users", User.class);
    }

    @Override
    public User findUser(UUID id) {
        return userMongoCollection.find(Filters.eq("_id", id)).first();
    }

    @Override
    public User findUser(String username) {
        return userMongoCollection.find(Filters.eq("username", username)).first();
    }

    //todo znajdywanie po wartosci stringa
    @Override
    public List<User> findUsersMatchToValue(String username) {
        return null;
    }

    @Override
    public User saveClient(User user) {
        user.setId(UUID.randomUUID());
        userMongoCollection.insertOne(user);
        return user;
    }

    @Override
    public List<User> findUsers() {
        return userMongoCollection.find().into(new ArrayList<>());
    }

    @Override
    public User setActive(UUID id, boolean active) {
        return userMongoCollection.findOneAndUpdate(Filters.eq("_id", id), Updates.combine(
                Updates.set("active", active)
        ));
    }

    @Override
    public User updateUser(UUID id, User user) {
        return userMongoCollection.findOneAndUpdate(Filters.eq("_id", id), Updates.combine(
                Updates.set("username", user.getUsername()),
                Updates.set("user_type", user.getUserType())
        ));
    }
}
