package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.ResourceNotFoundException;
import ba.unsa.etf.si.local_server.models.Product;
import ba.unsa.etf.si.local_server.models.transactions.ReceiptItem;
import ba.unsa.etf.si.local_server.repositories.ProductRepository;
import ba.unsa.etf.si.local_server.requests.FilterRequest;
import ba.unsa.etf.si.local_server.requests.ReceiptItemRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getProducts(FilterRequest filterRequest) {
        return productRepository.findByFilter(filterRequest);
    }

    public Product getProduct(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such product!"));
    }

    public void batchInsertProducts(List<Product> products) {
        productRepository.deleteAllInBatch();
        productRepository.flush();
        productRepository.saveAll(products);
        productRepository.flush();
    }

    public void updateProductQuantity(Long id, Double delta) {
       Product product = getProduct(id);
       product.setQuantity(product.getQuantity() + delta);
       productRepository.save(product);
    }

    public boolean checkProducts(List<ReceiptItemRequest> items) {
        AtomicBoolean log = new AtomicBoolean(true);

        items.forEach(receiptItem -> {
            if(!productRepository.findById(receiptItem.getId()).isPresent()) log.set(false);
        });
        return log.get();
    }

}
