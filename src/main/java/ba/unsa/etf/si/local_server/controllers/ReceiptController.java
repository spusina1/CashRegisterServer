package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.models.transactions.Receipt;
import ba.unsa.etf.si.local_server.services.ReceiptService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class ReceiptController {
    private final ReceiptService receiptService;

    @DeleteMapping(value = "/api/receipts/{id}")
    public ResponseEntity<Object> deleteReceipt(@PathVariable("id") Long id) {
        return receiptService.deleteReceipt(id);
    }

    //@GetMapping("/api/receipts")
    //public List<Receipt> getReceipts() {
    //    return receiptService.getReceipts();
    //}

}
