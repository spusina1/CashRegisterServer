package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.ResourceNotFoundException;
import ba.unsa.etf.si.local_server.models.Product;
import ba.unsa.etf.si.local_server.models.transactions.Receipt;
import ba.unsa.etf.si.local_server.models.transactions.ReceiptItem;
import ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus;
import ba.unsa.etf.si.local_server.repositories.ProductRepository;
import ba.unsa.etf.si.local_server.repositories.ReceiptRepository;
import ba.unsa.etf.si.local_server.requests.ReceiptRequest;
import ba.unsa.etf.si.local_server.requests.SellerAppRequest;
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
    private final ProductRepository productRepository;

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

    public String saveOrder(SellerAppRequest receiptItems) {
        if(receiptItems.getItems() != null){
            Instant instant = Instant.now();
            long timeStampMillis = instant.toEpochMilli();
            Receipt newReceipt = new Receipt();

            newReceipt.setReceiptStatus(ReceiptStatus.UNPROCESSED);
            newReceipt.setBusinessId(1L);
            newReceipt.setOfficeId(1L);
            //newReceipt.setCashRegisterId(1L);
            //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            //newReceipt.setUsername(auth.getPrincipal().toString());

            newReceipt.setTimestamp(timeStampMillis);

            Set<ReceiptItem> items = receiptItems
                    .getItems()
                    .stream()
                    .map(receiptItemRequest -> new ReceiptItem(null, receiptItemRequest.getId(), receiptItemRequest.getQuantity()))
                    .collect(Collectors.toSet());
            newReceipt.setReceiptItems(items);
            newReceipt.setTotalPrice(getTotalPrice(items));
            receiptRepository.save(newReceipt);
            return  "Order is successfully saved!";
        }
        return "";
    }

    public void updateReceipt(Long id) {
        Receipt receipt = receiptRepository.getOne(id);
        receipt.setReceiptStatus(PAYED);
        receiptRepository.save(receipt);
    }

    public  BigDecimal getTotalPrice(Set<ReceiptItem> items){
        BigDecimal sum = BigDecimal.valueOf(0);
        for(ReceiptItem item: items){
            Product product = productRepository
                    .findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("No such product!"));
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return sum;
    }



}
