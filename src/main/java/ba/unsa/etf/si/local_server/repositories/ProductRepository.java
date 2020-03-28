package ba.unsa.etf.si.local_server.repositories;

import ba.unsa.etf.si.local_server.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, FilteredProductRepository {

}
