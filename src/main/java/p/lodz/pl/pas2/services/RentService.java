package p.lodz.pl.pas2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.repositories.Implementations.RentRepositoryImplementation;
import p.lodz.pl.pas2.repositories.Implementations.mongoDB.RentRepositoryMongoDB;
import p.lodz.pl.pas2.repositories.RentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class RentService {
//    private final RentRepositoryMongoDB repository;

    private final RentRepositoryImplementation repository;
    @Autowired
    public RentService(RentRepositoryImplementation repository) {
        this.repository = repository;
    }

//    @Autowired
//    public RentService(RentRepositoryMongoDB repository) {
//        this.repository = repository;
//    }

    public Rent addRent(Rent rent) {
        if(rent.getStartDate().isBefore(LocalDate.now())) return null;
        if(rent.getEndDate() != null && rent.getEndDate().isBefore(rent.getStartDate())) return null;
        List<Rent> currentRents = repository.findCurrentRents();
        for(Rent cRent : currentRents) {
            if(cRent.getMovie().getId().equals(rent.getId())) return null;
        }
        return repository.saveRent(rent);
    }

    public boolean deleteRent(UUID id) {
        Rent rentToDelete = repository.findRent(id);
        if(rentToDelete == null) return false;
        if(rentToDelete.getEndDate().isAfter(LocalDate.now())) return false;
        return repository.deleteRent(id);
    }

    public Rent setEndTime(UUID id, LocalDate endTime) {
        Rent rentToUpdate = repository.findRent(id);
        if(endTime == null) return null;
        if(rentToUpdate.getStartDate().isAfter(endTime)) return null;
        return repository.updateEndTime(id, endTime);
    }

    public List<Rent> getCurrentRents() {
        return repository.findCurrentRents();
    }

    public List<Rent> getPastRents() {
        return repository.findPastRents();
    }
}
