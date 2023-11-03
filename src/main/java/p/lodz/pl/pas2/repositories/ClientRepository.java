package p.lodz.pl.pas2.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import p.lodz.pl.pas2.model.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends MongoRepository<Client, ObjectId> {
    Optional<Client> findClientById(ObjectId id);
}
