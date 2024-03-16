package p.lodz.pl.pas2.exceptions.rentExceptions;

public class RentForAnotherClientException extends RuntimeException {
    public RentForAnotherClientException(String message) {
        super(message);
    }
}
