package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.models.transactions.Receipt;
import ba.unsa.etf.si.local_server.models.transactions.ReceiptItem;
import ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus;
import ba.unsa.etf.si.local_server.repositories.ReceiptRepository;
import ba.unsa.etf.si.local_server.requests.ReceiptRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus.PAYED;

@AllArgsConstructor
@Service
public class ReceiptService {
    private final ReceiptRepository receiptRepository;

    public String checkRequest(ReceiptRequest receiptRequest) {
        boolean present = receiptRepository.findById(receiptRequest.getId()).isPresent();
        if(present) {
            ReceiptStatus receiptStatus = receiptRepository.getOne(receiptRequest.getId()).getReceiptStatus();
            if (receiptStatus == PAYED) return "Already processed request!";
            else{

            }

        }
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
            //newReceipt.setReceiptItems(receiptRequest.getReceiptItems());
            Set<ReceiptItem> items = receiptRequest
                    .getReceiptItems()
                    .stream()
                    .map(receiptItemRequest -> new ReceiptItem(null, receiptItemRequest.getId(), receiptItemRequest.getQuantity()))
                    .collect(Collectors.toSet());
            newReceipt.setReceiptItems(items);
            receiptRepository.save(newReceipt);
            return  "Receipt is successfully saved!";
        }
    return  "Nista";
    }

    public void updateReceipt(Long id) {
        Receipt receipt = receiptRepository.getOne(id);
        receipt.setReceiptStatus(PAYED);
        receiptRepository.save(receipt);
    }
}
