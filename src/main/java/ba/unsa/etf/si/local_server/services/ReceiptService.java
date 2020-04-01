package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.models.transactions.Receipt;
import ba.unsa.etf.si.local_server.repositories.ReceiptRepository;
import ba.unsa.etf.si.local_server.requests.ReceiptRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus.PAYED;

@AllArgsConstructor
@Service
public class ReceiptService {
    private final ReceiptRepository receiptRepository;

    public String checkRequest(ReceiptRequest receiptRequest) {
        Boolean log = receiptRepository.findById(receiptRequest.getId()).isPresent();
        System.out.println(log);
        if(log) return "Already processed request!"; //provjerit is paid
        else{
            Receipt newReceipt = new Receipt();
            newReceipt.setReceiptId("123");
            newReceipt.setReceiptStatus(PAYED);
            newReceipt.setBusinessId(1L);
            newReceipt.setOfficeId(1L);
            newReceipt.setCashRegisterId(receiptRequest.getCashRegisterId());
            newReceipt.setUsername(receiptRequest.getUsername());
            newReceipt.setTotalPrice(BigDecimal.valueOf(100));
            newReceipt.setTimestamp(100L);
            newReceipt.setReceiptItems(receiptRequest.getReceiptItems());

            receiptRepository.save(newReceipt);
            return  "Receipt is successfully saved!";
        }

    }
}
