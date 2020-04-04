package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.models.transactions.Receipt;
import ba.unsa.etf.si.local_server.models.transactions.Views;
import ba.unsa.etf.si.local_server.requests.LoginRequest;
import ba.unsa.etf.si.local_server.requests.ReceiptRequest;
import ba.unsa.etf.si.local_server.requests.SellerAppRequest;
import ba.unsa.etf.si.local_server.responses.LoginResponse;
import ba.unsa.etf.si.local_server.responses.ReceiptResponse;
import ba.unsa.etf.si.local_server.services.ReceiptService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
public class ReceiptController {
    private final ReceiptService receiptService;

    @DeleteMapping(value = "/api/orders/{id}")
    public ResponseEntity<Object> deleteReceipt(@PathVariable("id") Long id) {
        return receiptService.deleteReceipt(id);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<?> getSellerAppReceipts() {
        return ResponseEntity.ok(receiptService.getSellerReceipts());
    }
    
    @PostMapping("/api/receipts")
    public ResponseEntity<?> saveReceipt(@Valid @RequestBody ReceiptRequest receiptRequest) {
        String responseMessage = receiptService.checkRequest(receiptRequest);
        return ResponseEntity.ok(new ReceiptResponse(responseMessage));
    }

    @PostMapping("/api/orders")
    public ResponseEntity<?> saveOrder(@Valid @RequestBody SellerAppRequest receiptItems){
        String responseMessage = receiptService.saveOrder(receiptItems);
        return ResponseEntity.ok(new ReceiptResponse(responseMessage));
    }

}
