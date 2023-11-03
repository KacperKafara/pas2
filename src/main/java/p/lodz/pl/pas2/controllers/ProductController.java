package p.lodz.pl.pas2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.services.ProductService;
import p.lodz.pl.pas2.model.Product;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping(path = "/add")
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        return new ResponseEntity<Product>(productService.createProduct(product.getName(), product.getDescription(),
                product.getBasePrice(),product.getNumberOfProducts()), HttpStatus.CREATED);
    }

    @GetMapping(path = "/get")
    public ResponseEntity<List<Product>> getProducts(){
        return new ResponseEntity<List<Product>>(productService.getAll(), HttpStatus.OK);
    }
}
