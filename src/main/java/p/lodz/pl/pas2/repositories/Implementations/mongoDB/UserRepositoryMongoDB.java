package p.lodz.pl.pas2.repositories.Implementations.mongoDB;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import p.lodz.pl.pas2.exceptions.userExceptions.UsernameInUseException;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.msg.UserMsg;
import p.lodz.pl.pas2.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@Scope("singleton")
public class UserRepositoryMongoDB implements UserRepository {
    private final MongoCollection<User> userMongoCollection;
    private final FindOneAndUpdateOptions options;

    public UserRepositoryMongoDB(MongoCollection<User> userMongoCollection, FindOneAndUpdateOptions options) {
        this.userMongoCollection = userMongoCollection;
        this.userMongoCollection.createIndex(Indexes.ascending("username"),
                new IndexOptions().unique(true));
        this.options = options;
    }

    @Override
    public User findUser(UUID id) {
        return userMongoCollection.find(Filters.eq("_id", id)).first();
    }

    @Override
    public User findUser(String username) {
        return userMongoCollection.find(Filters.eq("username", username)).first();
    }

    @Override
    public List<User> findUsersMatchToValue(String username) {
        return userMongoCollection.find(Filters.regex("username", username)).into(new ArrayList<>());
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
        ), options);
    }

    @Override
    public User updateUser(UUID id, User user) {
        return userMongoCollection.findOneAndUpdate(Filters.eq("_id", id), Updates.combine(
                Updates.set("username", user.getUsername()),
                Updates.set("active", user.isActive())
        ), options);
    }

    @Override
    public List<User> findClients() {
        return userMongoCollection.find(Filters.eq("_clazz", "client")).into(new ArrayList<>());
    }

}