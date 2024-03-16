package p.lodz.pl.pas2.request;

public class AdministratorRequest extends UserRequest {
    public AdministratorRequest(String username, boolean active, String password) {
        super(username, active, password);
    }
}

