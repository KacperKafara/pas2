package p.lodz.pl.pas2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import p.lodz.pl.pas2.model.Product;
import p.lodz.pl.pas2.repositories.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

//    @Autowired
//    private MongoTemplate mongoTemplate;

    public Product createProduct(String name, String description, double price, int number){
        Product product = repository.insert( new Product(name, price, number, description));
        // Dodawanie nowego id do listy id w dokumencie dla innej klasy
//        mongoTemplate.update(Client.class)
//                .matching(Criteria.where("id").is(clientId))
//                .apply(new Update().push("productIds").value(product));
        return product;
    }

    public List<Product> getAll(){
        return repository.findAll();
    }
}
