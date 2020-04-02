package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.ResourceNotFoundException;
import ba.unsa.etf.si.local_server.models.Product;
import ba.unsa.etf.si.local_server.models.transactions.Receipt;
import ba.unsa.etf.si.local_server.models.transactions.ReceiptItem;
import ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus;
import ba.unsa.etf.si.local_server.repositories.ReceiptRepository;
import ba.unsa.etf.si.local_server.requests.ReceiptRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus.*;

@AllArgsConstructor
@Service
public class ReceiptService {
    private final ReceiptRepository receiptRepository;

    public String checkRequest(ReceiptRequest receiptRequest) {
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
                ReceiptStatus receiptStatus = receiptRepository.getOne(receiptRequest.getId()).getReceiptStatus();
                System.out.println(receiptStatus);
                if (receiptStatus == PAYED) return "Already processed request!";
                if(receiptStatus == PENDING){
                    updateReceipt(receiptRequest.getId());
                    return "Receipt is successfully saved!";
                }
            }
        }
        return  "";
    }

    public String removeReceipt(Long id){
        if(getReceipt(id) == null) return "No receipt with id " + id;
        Receipt receipt = makeNegative(getReceipt(id));
        receiptRepository.save(receipt);
        return "Receipt successfully deleted!";

    }

    private Receipt makeNegative(Receipt receipt){
        receipt.setId(receipt.getId() * -1);
        for(ReceiptItem receiptItem : receipt.getReceiptItems()){
            receiptItem.setQuantity(receiptItem.getQuantity() * -1);
        }
        receipt.setReceiptStatus(DELETED);
        return receipt;
    }

    public void updateReceipt(Long id) {
        Receipt receipt = receiptRepository.getOne(id);
        receipt.setReceiptStatus(PAYED);
        receiptRepository.save(receipt);
    }

    public Receipt getReceipt(Long id) {
        return receiptRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such receipt!"));
    }

    public  BigDecimal getTotalPrice(Set<ReceiptItem> items){
        return BigDecimal.valueOf(items
                .stream()
                .mapToDouble(ReceiptItem::getQuantity)
                .sum());
    }
}
