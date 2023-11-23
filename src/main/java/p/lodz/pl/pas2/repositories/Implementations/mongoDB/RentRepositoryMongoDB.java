package p.lodz.pl.pas2.repositories.Implementations.mongoDB;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.BsonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.repositories.RentRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@Scope("singleton")
public class RentRepositoryMongoDB implements RentRepository {
    private final MongoCollection<Rent> rentMongoCollection;

    private final FindOneAndUpdateOptions options;

    @Autowired
    public RentRepositoryMongoDB(MongoDatabase mongoRepo, FindOneAndUpdateOptions options) {
        this.rentMongoCollection = mongoRepo.getCollection("rents", Rent.class);
        this.options = options;
    }

    @Override
    public Rent findRent(UUID id) {
        return rentMongoCollection.find(Filters.eq("_id", id)).first();
    }

    @Override
    public Rent saveRent(Rent rent) {
        rent.setId(UUID.randomUUID());
        rentMongoCollection.insertOne(rent);
        return rent;
    }

    @Override
    public boolean deleteRent(UUID id) {
        Rent rent = rentMongoCollection.findOneAndDelete(Filters.eq("_id", id));
        return rent != null;
    }
    // TODO do sprawdzenia czy działaja metody find past i current rents
    @Override
    public List<Rent> findCurrentRents() {
        LocalDate currentDate = LocalDate.now();
        return rentMongoCollection.find(Filters.and(
                Filters.or(
                        Filters.eq("start_date", currentDate),
                        Filters.lt("start_date", currentDate)
                ),
                Filters.or(
                        Filters.eq("end_date", BsonNull.VALUE),
                        Filters.gt("end_date", currentDate)
                )
        )).into(new ArrayList<>());
    }

    @Override
    public List<Rent> findPastRents() {
        LocalDate currentDate = LocalDate.now();
        return rentMongoCollection.find(Filters.and(
                Filters.ne("end_date", BsonNull.VALUE),
                Filters.lte("end_date", currentDate)
        )).into(new ArrayList<>());

    }

    @Override
    public Rent updateEndTime(UUID id, LocalDate endTime) {
        return rentMongoCollection.findOneAndUpdate(Filters.eq("_id", id), Updates.combine(
                        Updates.set("end_date", endTime)),
                options);
    }
}