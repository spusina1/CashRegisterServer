package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.services.ReceiptService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class ReceiptController {
    private final ReceiptService receiptService;
}
