package p.lodz.pl.pas2.services;

import org.springframework.beans.factory.annotation.Autowired;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.repositories.RentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class RentService {
    private final RentRepository repository;

    @Autowired
    public RentService(RentRepository repository) {
        this.repository = repository;
    }

    public Rent getRent(UUID id) {
        return repository.findRent(id);
    }

    public List<Rent> getRents() {
        return repository.findRents();
    }

    public Rent addRent(Rent rent) {
        if(rent.getEndDate().isBefore(rent.getStartDate())) return null;
        return repository.saveRent(rent);
    }

    public Rent updateRent(UUID id, Rent rent) {
        if(rent.getEndDate().isBefore(rent.getStartDate())) return null;
        return repository.updateRent(id, rent);
    }

    public boolean deleteRent(UUID id) {
        Rent rentToDelete = repository.findRent(id);
        if(rentToDelete == null) return false;
        if(rentToDelete.getEndDate().isAfter(LocalDate.now())) return false;
        return repository.deleteRent(id);
    }

    public List<Rent> getCurrentRents() {
        return repository.findCurrentRents();
    }

    public List<Rent> getPastRents() {
        return repository.findPastRents();
    }
}
