package ba.unsa.etf.si.local_server.repositories;

import ba.unsa.etf.si.local_server.models.transactions.Receipt;
import ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Optional<Receipt> findByReceiptId(String receiptId);
    List<Receipt> findByCashRegisterId(Long cashRegisterId);
    Set<Receipt> findReceiptByReceiptStatus(ReceiptStatus receiptStatus);
}
