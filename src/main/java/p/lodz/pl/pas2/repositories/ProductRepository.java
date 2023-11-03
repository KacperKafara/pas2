package p.lodz.pl.pas2.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import p.lodz.pl.pas2.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, ObjectId> {
}
