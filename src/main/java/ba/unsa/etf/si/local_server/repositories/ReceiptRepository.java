package ba.unsa.etf.si.local_server.repositories;

import ba.unsa.etf.si.local_server.models.transactions.Receipt;
import ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findReceiptByReceiptStatus(ReceiptStatus receiptStatus);
}
