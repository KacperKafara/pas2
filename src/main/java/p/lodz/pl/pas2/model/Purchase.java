package p.lodz.pl.pas2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Map;

@Document("purchases")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Purchase extends AbstractEntity{


    private LocalDate purchaseDate;

    private LocalDate deliveryDate;


    private double finalCost;


    private Client client;


    private Map<Product, Integer> products;

    public Purchase(Client client, Map<Product, Integer> products) {
        this.client = client;
        this.products = products;
        purchaseDate = LocalDate.now();
        setDeliveryTime();
        setFinalCost();
        client.addMoneySpent(finalCost);
    }

    public Purchase( LocalDate purchaseDate,
                     LocalDate deliveryDate,
                     double finalCost,
                    Client client,
                     Map<Product, Integer> products){
        this.purchaseDate = purchaseDate;
        this.deliveryDate = deliveryDate;
        this.finalCost = finalCost;
        this.client = client;
        this.products = products;

    }

    private void setDeliveryTime(){
        deliveryDate = purchaseDate.plusDays(3 - client.getClientShorterDeliveryTime());
    }

    private void setFinalCost(){
//        for(Product product : products) {
//            finalCost += product.getBaseCost() -
//                    product.getBaseCost() * product.getDiscount() -
//                    client.getClientDiscount() * product.getBaseCost();
//        }

    }
}