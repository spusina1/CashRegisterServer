package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.requests.LoginRequest;
import ba.unsa.etf.si.local_server.requests.ReceiptRequest;
import ba.unsa.etf.si.local_server.responses.LoginResponse;
import ba.unsa.etf.si.local_server.responses.ReceiptResponse;
import ba.unsa.etf.si.local_server.services.ReceiptService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
public class ReceiptController {
    private final ReceiptService receiptService;

    @PostMapping("/api/receipts")
    public ResponseEntity<?> saveReceipt(@Valid @RequestBody ReceiptRequest receiptRequest) {
        String responseMessage = receiptService.checkRequest(receiptRequest);
        return ResponseEntity.ok(new ReceiptResponse(responseMessage));
    }
}
