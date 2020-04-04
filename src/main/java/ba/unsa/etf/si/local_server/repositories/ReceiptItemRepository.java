package ba.unsa.etf.si.local_server.repositories;

import ba.unsa.etf.si.local_server.models.transactions.Receipt;
import ba.unsa.etf.si.local_server.models.transactions.ReceiptItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptItemRepository  extends JpaRepository<ReceiptItem, Long> {
}
