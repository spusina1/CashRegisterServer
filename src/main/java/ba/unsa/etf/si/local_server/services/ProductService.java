package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.ResourceNotFoundException;
import ba.unsa.etf.si.local_server.models.Product;
import ba.unsa.etf.si.local_server.repositories.ProductRepository;
import ba.unsa.etf.si.local_server.requests.FilterRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Product> getProducts(FilterRequest filterRequest) {
        List<Product> products = productRepository.findAll();
        Stream<Product> productStream = products.stream();

        if(filterRequest.getDiscount()) {
        }

        return productStream.collect(Collectors.toList());
    }

    public Product getProduct(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such product!"));
    }

    public String getFromMain() {
        return restTemplate.getForObject("https://main-server-si.herokuapp.com/api/logs", String.class);
    }

}
