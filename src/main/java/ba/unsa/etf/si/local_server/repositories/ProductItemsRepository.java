package ba.unsa.etf.si.local_server.repositories;

import ba.unsa.etf.si.local_server.models.Product;
import ba.unsa.etf.si.local_server.models.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductItemsRepository extends JpaRepository<ProductItem, Long>, FilteredProductRepository{
}
