package p.lodz.pl.pas2.dto;


import p.lodz.pl.pas2.model.Client;

public record ClientDTO(
        String fName,
        String lName,
        Client.Type clientType

) {

}
