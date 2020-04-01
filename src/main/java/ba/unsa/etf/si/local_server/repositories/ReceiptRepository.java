package ba.unsa.etf.si.local_server.repositories;

import ba.unsa.etf.si.local_server.models.transactions.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
