package p.lodz.pl.pas2.repositories.Implementations.mongoDB;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;
import org.springframework.stereotype.Repository;
import p.lodz.pl.pas2.exceptions.userExceptions.UsernameInUseException;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.msg.UserMsg;
import p.lodz.pl.pas2.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserRepositoryMongoDB implements UserRepository {
    private final MongoCollection<User> userMongoCollection;

    public UserRepositoryMongoDB(MongoCollection<User> userMongoCollection) {
        this.userMongoCollection = userMongoCollection;
        this.userMongoCollection.createIndex(Indexes.ascending("username"),
                new IndexOptions().unique(true));
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
        if (isUsernameUnique(user.getUsername())) {
            user.setId(UUID.randomUUID());
            userMongoCollection.insertOne(user);
            return user;
        } else {
            throw new UsernameInUseException(UserMsg.USERNAME_IN_USE);
        }
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
                Updates.set("active", user.isActive())
        ));
    }

    private boolean isUsernameUnique(String username) {
        long count = userMongoCollection.countDocuments(Filters.eq("username", username));
        return count == 0;
    }
}