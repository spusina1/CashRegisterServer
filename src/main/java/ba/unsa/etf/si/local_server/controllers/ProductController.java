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
    public List<Product> getProducts(
            @RequestParam(required = false) Boolean discount,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) Boolean descending,
            @RequestParam(required = false) Integer lowestPrice,
            @RequestParam(required = false) Integer highestPrice,
            @RequestParam(required = false) String search
    ) {
        FilterRequest filterRequest = new FilterRequest(
                orderBy, descending, discount, lowestPrice, highestPrice, search
        );
        return productService.getProducts(filterRequest);
    }

    @GetMapping("/api/products/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

}
