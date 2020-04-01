package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.models.transactions.Receipt;
import ba.unsa.etf.si.local_server.models.transactions.Views;
import ba.unsa.etf.si.local_server.services.ReceiptService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Map;
import java.util.TreeMap;


@AllArgsConstructor
@RestController
public class ReceiptController {
    private final ReceiptService receiptService;

    @GetMapping("/api/sellerAppReceipts")
    @JsonView(Views.Public.class)
    public Map<String, List<Receipt>> getSAReceipts() {
        List<Receipt> result = receiptService.getReceipts();
        Map<String, List<Receipt>> map = new TreeMap<>();
        map.put("receipts", result);
        return map;
    }
}
