package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.models.transactions.Receipt;
import ba.unsa.etf.si.local_server.models.transactions.ReceiptItem;
import ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus;
import ba.unsa.etf.si.local_server.repositories.ReceiptRepository;
import ba.unsa.etf.si.local_server.requests.ReceiptRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus.PAYED;
import static ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus.PENDING;

@AllArgsConstructor
@Service
public class ReceiptService {
    private final ReceiptRepository receiptRepository;

    public String checkRequest(ReceiptRequest receiptRequest, ReceiptStatus receiptStatus) {

        if(receiptRequest.getId()==null){
            Instant instant = Instant.now();
            long timeStampMillis = instant.toEpochMilli();
            Receipt newReceipt = new Receipt();
            newReceipt.setReceiptId(receiptRequest.getCashRegisterId()+ "11" + timeStampMillis);
            newReceipt.setReceiptStatus(PAYED);
            newReceipt.setBusinessId(1L);
            newReceipt.setOfficeId(1L);
            newReceipt.setCashRegisterId(receiptRequest.getCashRegisterId());
            newReceipt.setUsername(receiptRequest.getUsername());
            newReceipt.setTimestamp(timeStampMillis);

            Set<ReceiptItem> items = receiptRequest
                    .getReceiptItems()
                    .stream()
                    .map(receiptItemRequest -> new ReceiptItem(null, receiptItemRequest.getId(), receiptItemRequest.getQuantity()))
                    .collect(Collectors.toSet());
            newReceipt.setReceiptItems(items);
            newReceipt.setTotalPrice(getTotalPrice(items));
            receiptRepository.save(newReceipt);
            return  "Receipt is successfully saved!";
        }
        else{

            boolean present = receiptRepository.findById(receiptRequest.getId()).isPresent(); //znaci da nije obrisan racun
            if(present) {
                System.out.println(receiptStatus);
                /*if (receiptStatus == PAYED) return "Already processed request!";
                if(receiptStatus == PENDING){
                    updateReceipt(receiptRequest.getId());
                    return "Receipt is successfully saved!";
                }*/
                switch (receiptStatus){
                    case PAYED:
                        return "Already processed request!";
                    case PENDING:
                        updateReceipt(receiptRequest.getId());
                        return "Receipt is successfully saved!";
                    case AWAITING_PAYMENT:
                        Receipt receipt = receiptRepository.getOne(receiptRequest.getId());
                        receiptRepository.save(receipt);
                        return "Receipt awaiting payment!";
                }
            }
        }
        return  "";
    }



    public void updateReceipt(Long id) {
        Receipt receipt = receiptRepository.getOne(id);
        receipt.setReceiptStatus(PAYED);
        receiptRepository.save(receipt);
    }

    public  BigDecimal getTotalPrice(Set<ReceiptItem> items){
        return BigDecimal.valueOf(items
                .stream()
                .mapToDouble(ReceiptItem::getQuantity)
                .sum());
    }
}
