package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.repositories.ReceiptRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
}
