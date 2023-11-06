package p.lodz.pl.pas2.repositories.Implementations;

import org.springframework.stereotype.Repository;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.repositories.RentRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class RentRepositoryImplementation implements RentRepository {
    private final List<Rent> rents;

    public RentRepositoryImplementation() {
        this.rents = new ArrayList<>();
    }

    @Override
    public Rent findRent(UUID id) {
        for (Rent rent : rents) {
            if (rent.getId().equals(id)) {
                return rent;
            }
        }
        return null;
    }

    @Override
    public Rent saveRent(Rent rent) {
        rent.setId(UUID.randomUUID());
        rents.add(rent);
        return rent;
    }

    @Override
    public List<Rent> findRents() {
        return rents;
    }

    @Override
    public Rent updateRent(UUID id, Rent rent) {
        Rent updatedRent = findRent(id);
        updatedRent.setUser(rent.getUser());
        updatedRent.setMovie(rent.getMovie());
        updatedRent.setStartDate(rent.getStartDate());
        updatedRent.setEndDate(rent.getEndDate());
        return updatedRent;
    }

    @Override
    public boolean deleteRent(UUID id) {
        for (Rent rent : rents) {
            if (rent.getId().equals(id)) {
                rents.remove(rent);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Rent> findCurrentRents() {
        LocalDate currentDate = LocalDate.now();
        List<Rent> currentRents = new ArrayList<>();

        for (Rent rent : rents) {
            if (rent.getStartDate().isBefore(currentDate) && rent.getEndDate().isAfter(currentDate)) {
                currentRents.add(rent);
            }
        }

        return currentRents;
    }

    @Override
    public List<Rent> findPastRents() {
        LocalDate currentDate = LocalDate.now();
        List<Rent> pastRents = new ArrayList<>();

        for (Rent rent : rents) {
            if (rent.getEndDate().isBefore(currentDate)) {
                pastRents.add(rent);
            }
        }

        return pastRents;
    }
}
