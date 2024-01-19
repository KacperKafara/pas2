package p.lodz.pl.pas2.request;

public class ModeratorRequest extends UserRequest {
    public ModeratorRequest(String username, boolean active, String password) {
        super(username, active, password);
    }
}
