package ba.unsa.etf.si.local_server.repositories;

import ba.unsa.etf.si.local_server.models.transactions.Receipt;
import ba.unsa.etf.si.local_server.models.transactions.ReceiptItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReceiptItemRepository  extends JpaRepository<ReceiptItem, Long> {

    @Modifying
    @Transactional
    @Query(
            nativeQuery = true,
            value = "delete from receipt_items where receipt_id = ?1"
    )
    void deleteByReceipt(Long id);
}
