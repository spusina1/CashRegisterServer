package ba.unsa.etf.si.local_server.repositories;

import ba.unsa.etf.si.local_server.models.Product;
import ba.unsa.etf.si.local_server.requests.FilterRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Repository
public class FilteredProductRepositoryImpl implements FilteredProductRepository {
    private final EntityManager em;

    @Override
    public List<Product> findByFilter(FilterRequest filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);

        Root<Product> root = cq.from(Product.class);
        List<Predicate> predicates = new ArrayList<>();

        if(filter.getDiscount() != null) {
            if(filter.getDiscount()) {
                predicates.add(cb.greaterThan(root.get("discount"), 0));
            } else {
                predicates.add(cb.equal(root.get("discount"), 0));
            }
        }

        if(filter.getMinPrice() != null && filter.getMinPrice().compareTo(0) >= 0) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
        }

        if(filter.getMaxPrice() != null && filter.getMaxPrice().compareTo(0) >= 0) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
        }

        if(filter.getName() != null && !filter.getName().isEmpty()) {
            String pattern = String.format("%%%s%%", filter.getName());
            predicates.add(cb.like(root.get("name"), pattern));
        }

        try {
            if(filter.getOrderBy() != null) {
                Expression<Object> expression = root.get(filter.getOrderBy());
                boolean isDescending = filter.getDescending() != null && filter.getDescending();
                cq.orderBy(isDescending ? cb.desc(expression) : cb.asc(expression));
            }
        } catch(IllegalArgumentException ignore) {}

        cq.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(cq).getResultList();
    }
}
