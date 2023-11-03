package p.lodz.pl.pas2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class Address implements Serializable {


    private String city;
    private String street;
    private String number;

//    public String getAddressInfo(){
//        return city + " " + street + " " + number;
//    }

//    public Address(String city, String street, String number) {
//        this.city = city;
//        this.street = street;
//        this.number = number;
//    }


    public Address(String city,
                   String street,
                    String number){
        this.city =city;
        this.street = street;
        this.number = number;
    }
}
