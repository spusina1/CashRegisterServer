package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.models.transactions.Receipt;
import ba.unsa.etf.si.local_server.requests.EditOrderRequest;
import ba.unsa.etf.si.local_server.requests.GuestOrderRequest;
import ba.unsa.etf.si.local_server.requests.ReceiptRequest;
import ba.unsa.etf.si.local_server.requests.SellerAppRequest;
import ba.unsa.etf.si.local_server.responses.Response;
import ba.unsa.etf.si.local_server.services.ReceiptService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
public class ReceiptController {
    private final ReceiptService receiptService;

    @Secured({"ROLE_OFFICEMAN", "ROLE_CASHIER"})
    @GetMapping("/api/orders")
    public ResponseEntity<?> getSellerAppReceipts() {
        return ResponseEntity.ok(receiptService.getSellerReceipts());
    }

    @Secured({"ROLE_BARTENDER", "ROLE_GUEST"})
    @PostMapping("/api/orders")
    public ResponseEntity<?> saveOrder(@Valid @RequestBody SellerAppRequest receiptItems){
        String responseMessage = receiptService.saveOrder(receiptItems);
        return ResponseEntity.ok(new Response(responseMessage));
    }

    @Secured({"ROLE_CASHIER", "ROLE_BARTENDER", "ROLE_GUEST"})
    @PutMapping("api/orders")
    public  ResponseEntity<?> editOrder(@Valid @RequestBody EditOrderRequest editOrderRequest){
        String responseMessage = receiptService.editOrder(editOrderRequest);
        return  ResponseEntity.ok(new Response(responseMessage));
    }

    @Secured({"ROLE_CASHIER", "ROLE_BARTENDER"})
    @DeleteMapping("/api/orders/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable("id") Long id) {
        return receiptService.deleteReceipt(id);
    }

    @Secured({"ROLE_OFFICEMAN", "ROLE_BARTENDER"})
    @GetMapping("/api/guest-orders")
    public ResponseEntity<?> getGuestOrders() {
        return ResponseEntity.ok(receiptService.getGuestReceipts());
    }

    @Secured({"ROLE_BARTENDER"})
    @PostMapping("/api/guest-orders")
    public ResponseEntity<?> saveGuestOrder(@Valid @RequestBody GuestOrderRequest guestOrderRequest){
        String responseMessage = receiptService.saveGuestOrder(guestOrderRequest);
        return ResponseEntity.ok(new Response(responseMessage));
    }

    @Secured({"ROLE_OFFICEMAN", "ROLE_CASHIER"})
    @GetMapping("/api/receipts")
    public ResponseEntity<?> getReceipts(@RequestParam(name = "cash_register_id", required = false) Long cashRegisterId) {
        List<Receipt> receipts = receiptService.getReceipts(cashRegisterId);
        return ResponseEntity.ok(receipts);
    }

    @Secured({"ROLE_CASHIER"})
    @PostMapping("/api/receipts")
    public ResponseEntity<?> saveReceipt(@Valid @RequestBody ReceiptRequest receiptRequest) {
        String responseMessage = receiptService.checkRequest(receiptRequest);
        return ResponseEntity.ok(new Response(responseMessage));
    }

    @Secured({"ROLE_OFFICEMAN", "ROLE_CASHIER"})
    @GetMapping("/api/receipts/{id}")
    public ResponseEntity<?> getReceipt(@PathVariable String id) {
        Receipt receipt = receiptService.getReceipt(id);
        return ResponseEntity.ok(receipt);
    }

    @Secured({"ROLE_CASHIER"})
    @DeleteMapping("/api/receipts/{id}")
    public ResponseEntity<?> reverseReceipt(@PathVariable String id){
        String responseMessage = receiptService.reverseReceipt(id);
        return ResponseEntity.ok(new Response(responseMessage));
    }

    @Secured({"ROLE_OFFICEMAN", "ROLE_CASHIER"})
    @GetMapping("/api/report")
    public ResponseEntity<?> getDailyReceipts(@RequestParam(name = "cash_register_id", required = false) Long cashRegisterId) {
        List<Receipt> receipts = receiptService.getDailyReceipts(cashRegisterId);
        return ResponseEntity.ok(receipts);
    }

}
