package p.lodz.pl.pas2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import p.lodz.pl.pas2.model.type.ClientType;

@Document(collection = "clients")
@Getter
@Setter
@NoArgsConstructor
public class Client {

    public enum Type{
        STANDARD,
        PREMIUM,
        PREMIUM_DELUXE
    }

    @Id
    private ObjectId id;
    private String fName;
    private String lName;
    private boolean archived = false;
    private double moneySpent = 0.0;
    private Type clientType;

    public int getClientShorterDeliveryTime(){
        if (this.clientType == Type.STANDARD){
            return 0;
        } else if(this.clientType == Type.PREMIUM){
            return 1;
        } else {
            return 2;
        }
//        return clientType.getShorterDeliveryTime();
    }

    public void addMoneySpent(double value){
        moneySpent += value;
    }

    public Client(String fName, String lName, Type clientType) {
        this.id = new ObjectId();
        this.fName = fName;
        this.lName = lName;
        this.clientType = Type.PREMIUM;
    }
}
