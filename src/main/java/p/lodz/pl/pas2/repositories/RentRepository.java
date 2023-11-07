package p.lodz.pl.pas2.repositories;

import p.lodz.pl.pas2.model.Rent;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RentRepository {
    Rent findRent(UUID id);
    Rent saveRent(Rent rent);
    boolean deleteRent(UUID id);
    List<Rent> findCurrentRents();
    List<Rent> findPastRents();
    Rent updateEndTime(UUID id, LocalDate endTime);
}
