package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.models.Product;
import ba.unsa.etf.si.local_server.requests.FilterRequest;
import ba.unsa.etf.si.local_server.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class ProductController {
    private final ProductService productService;

    @GetMapping("/api/products")
    public List<Product> getProducts(@RequestParam(required = false) Boolean discount) {
        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setDiscount(discount);

        return productService.getProducts(filterRequest);
    }

    @GetMapping("/api/products/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

}
