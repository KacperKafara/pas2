package p.lodz.pl.pas2.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import p.lodz.pl.pas2.exceptions.movieExceptions.MovieInUseException;
import p.lodz.pl.pas2.exceptions.rentExceptions.*;
import p.lodz.pl.pas2.exceptions.userExceptions.UserNotActiveException;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.msg.MovieMsg;
import p.lodz.pl.pas2.msg.RentMsg;
import p.lodz.pl.pas2.msg.UserMsg;
import p.lodz.pl.pas2.repositories.RentRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class RentService {
    private final RentRepository repository;

    public RentService(@Qualifier("rentRepositoryMongoDB") RentRepository repository) {
        this.repository = repository;
    }

    public Rent getRent(UUID id) {
        Rent rent = repository.findRent(id);
        if(rent == null) throw new RentNotFoundException(RentMsg.RENT_NOT_FOUND);
        return rent;
    }

    public Rent addRent(Rent rent) {
        if(!rent.getUser().isActive()) throw new UserNotActiveException(UserMsg.USER_NOT_ACTIVE);
        if(rent.getStartDate().isBefore(LocalDate.now())) throw new StartDateException(RentMsg.WRONG_START_DATE);
        if(rent.getEndDate() != null && rent.getEndDate().isBefore(rent.getStartDate())) {
            throw new EndDateException(RentMsg.WRONG_END_DATE);
        }
        List<Rent> currentRents = repository.findCurrentRents();
        if (!repository.findCurrentRentsById(rent.getMovie().getId()).isEmpty()){
            throw new MovieInUseException(MovieMsg.MOVIE_IS_RENTED);
        }

//        for(Rent cRent : currentRents) {
//            if(cRent.getMovie().getId().equals(rent.getMovie().getId())) {
//                throw new MovieInUseException(MovieMsg.MOVIE_IS_RENTED);
//            }
//        }
        return repository.saveRent(rent);
    }

    public boolean deleteRent(UUID id) {
        Rent rentToDelete = repository.findRent(id);
        if(rentToDelete == null) throw new ThereIsNoSuchRentToDelete(RentMsg.RENT_NOT_FOUND);
        if(rentToDelete.getEndDate() == null || rentToDelete.getEndDate().isAfter(LocalDate.now())) throw new RentalStillOngoingException(RentMsg.RENT_NOT_ENDED);
        return repository.deleteRent(id);
    }

    public Rent setEndTime(UUID id, LocalDate endTime) {
        Rent rentToUpdate = repository.findRent(id);
        if(rentToUpdate == null) throw new ThereIsNoSuchRentToUpdateException(RentMsg.RENT_NOT_FOUND);
        if(rentToUpdate.getStartDate().isAfter(endTime)) throw new EndDateException(RentMsg.WRONG_END_DATE);
        if(rentToUpdate.getEndDate() != null) throw new RentIsAlreadyEndedException(RentMsg.RENT_IS_ALREADY_ENDED);
        return repository.updateEndTime(id, endTime);
    }

    public List<Rent> getCurrentRents() {
        List<Rent> rents = repository.findCurrentRents();
        if(rents.isEmpty()) throw new RentsNotFoundException(RentMsg.RENTS_NOT_FOUND);
        return rents;
    }

    public List<Rent> getCurrentRentsByClient(UUID clientId) {
        List<Rent> rents1 = repository.findCurrentRents();
        List<Rent> rents = rents1.stream().filter(element -> (element.getUser().getId().equals(clientId))).toList();
        if(rents.isEmpty()) throw new RentsNotFoundException(RentMsg.RENTS_NOT_FOUND);
        return rents;
    }

    public List<Rent> getCurrentRentsByMovie(UUID movieId) {
        List<Rent> rents = repository.findCurrentRents().stream().filter(element -> (element.getMovie().getId().equals(movieId))).toList();
        if(rents.isEmpty()) throw new RentsNotFoundException(RentMsg.RENTS_NOT_FOUND);
        return rents;
    }

    public List<Rent> getPastRentsByClient(UUID clientId) {
        List<Rent> rents = repository.findPastRents().stream().filter(element -> (element.getUser().getId().equals(clientId))).toList();
        if(rents.isEmpty()) throw new RentsNotFoundException(RentMsg.RENTS_NOT_FOUND);
        return rents;
    }

    public List<Rent> getPastRentsByMovie(UUID movieId) {
        List<Rent> rents = repository.findPastRents().stream().filter(element -> (element.getMovie().getId().equals(movieId))).toList();
        if(rents.isEmpty()) throw new RentsNotFoundException(RentMsg.RENTS_NOT_FOUND);
        return rents;
    }

    public List<Rent> getPastRents() {
        List<Rent> rents = repository.findPastRents();
        if(rents.isEmpty()) throw new RentsNotFoundException(RentMsg.RENTS_NOT_FOUND);
        return rents;
    }
}
