package p.lodz.pl.pas2.repositories.Implementations.mongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.BsonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.repositories.RentRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@Repository
public class RentRepositoryMongoDB implements RentRepository {


//    private final AbstractMongoRepository repository;

    private final MongoCollection<Rent> rentMongoCollection;

//    @Autowired
    public RentRepositoryMongoDB(
//            AbstractMongoRepository repository,
            MongoClient mongoClient) {
//        this.repository = repository;
//        this.rentMongoCollection = repository.getDatabase().getCollection("rents", Rent.class);
        this.rentMongoCollection = mongoClient.getDatabase("online-shop").getCollection("rents", Rent.class);
    }

    @Override
    public Rent findRent(UUID id) {
        return rentMongoCollection.find(Filters.eq("_id", id)).first();
    }

    @Override
    public Rent saveRent(Rent rent) {
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
                Filters.lt("end_date", currentDate)

        )).into(new ArrayList<>());
    }

    @Override
    public Rent updateEndTime(UUID id, LocalDate endTime) {
        return rentMongoCollection.findOneAndUpdate(Filters.eq("_id", id), Updates.combine(
                Updates.set("endTime", endTime)

        ));
    }
}
