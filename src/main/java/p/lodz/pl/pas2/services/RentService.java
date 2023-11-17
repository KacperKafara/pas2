package p.lodz.pl.pas2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import p.lodz.pl.pas2.exceptions.*;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.msg.MovieMsg;
import p.lodz.pl.pas2.msg.RentMsg;
import p.lodz.pl.pas2.repositories.RentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class RentService {
    private final RentRepository repository;
   // @Autowired
    public RentService(@Qualifier("rentRepositoryMongoDB") RentRepository repository) {
        this.repository = repository;
    }

    public Rent addRent(Rent rent) {
        if(rent.getStartDate().isBefore(LocalDate.now())) throw new StartDateException(RentMsg.WRONG_START_DATE);
        if(rent.getEndDate() != null && rent.getEndDate().isBefore(rent.getStartDate())) {
            throw new EndDateException(RentMsg.WRONG_END_DATE);
        }
        List<Rent> currentRents = repository.findCurrentRents();
        for(Rent cRent : currentRents) {
            if(cRent.getMovie().getId().equals(rent.getMovie().getId())) {
                throw new MovieException(MovieMsg.MOVIE_IS_RENTED);
            }
        }
        return repository.saveRent(rent);
    }

    public boolean deleteRent(UUID id) {
        Rent rentToDelete = repository.findRent(id);
        if(rentToDelete == null) throw new RentNotExistException(RentMsg.RENT_NOT_FOUND);
        if(rentToDelete.getEndDate() == null || rentToDelete.getEndDate().isAfter(LocalDate.now())) throw new RentalStillOngoingException(RentMsg.RENT_NOT_ENDED);
        return repository.deleteRent(id);
    }

    public Rent setEndTime(UUID id, LocalDate endTime) {
        Rent rentToUpdate = repository.findRent(id);
        if(rentToUpdate.getStartDate().isAfter(endTime)) throw new EndDateException(RentMsg.WRONG_END_DATE);
        return repository.updateEndTime(id, endTime);
    }

    public List<Rent> getCurrentRents() {
        return repository.findCurrentRents();
    }

    public List<Rent> getPastRents() {
        return repository.findPastRents();
    }
}
