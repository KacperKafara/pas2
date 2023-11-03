package p.lodz.pl.pas2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    private ObjectId id;
    private String name;
    private double basePrice;
    private double discount;
    private int numberOfProducts;
    private String description;
    private boolean archived = false;

    public Product(String name, double basePrice, int numberOfProducts, String description) {
        id = new ObjectId();
        this.name = name;
        this.basePrice = basePrice;
        this.numberOfProducts = numberOfProducts;
        this.description = description;
    }
}