package ba.unsa.etf.si.local_server.repositories;

import ba.unsa.etf.si.local_server.models.Product;
import ba.unsa.etf.si.local_server.requests.FilterRequest;

import java.util.List;

public interface FilteredProductRepository {
    List<Product> findByFilter(FilterRequest filter);
}
