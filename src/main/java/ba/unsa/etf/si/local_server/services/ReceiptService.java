package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.responses.SellerAppReceiptItemsResponse;
import ba.unsa.etf.si.local_server.responses.SellerAppReceiptsResponse;
import ba.unsa.etf.si.local_server.responses.DeleteReceiptResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

import static ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus.*;

@AllArgsConstructor
@Service
public class ReceiptService {
    private final ReceiptRepository receiptRepository;

    public ResponseEntity<Object> deleteReceipt(Long id){
        Optional<Receipt> receipt = receiptRepository.findById(id);

        if(receipt.isPresent()) {
            receiptRepository.deleteById(id);
            return new ResponseEntity<>(new DeleteReceiptResponse("Receipt is successfully deleted!"), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(new DeleteReceiptResponse("Already processed request!"), HttpStatus.BAD_REQUEST);
    }

    public Set<SellerAppReceiptsResponse> getSellerReceipts() {
        Set<Receipt> receipts = receiptRepository.findReceiptByReceiptStatus(UNPROCESSED);

        Set<SellerAppReceiptsResponse> sellerAppReceiptsResponses = receipts
                .stream()
                .map(r -> new SellerAppReceiptsResponse(r.getId(), r.getReceiptItems()
                        .stream()
                        .map(receiptItem -> new SellerAppReceiptItemsResponse(receiptItem.getProductId(), receiptItem.getQuantity()))
                        .collect(Collectors.toSet())))
                .collect(Collectors.toSet());
        return  sellerAppReceiptsResponses;
    }

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
            if(receiptStatus == UNPROCESSED){
                updateReceipt(receiptRequest.getId(), receiptRequest);
                return "Receipt is successfully saved!";
            }
             }
        }
          return  "";
    }

    public String saveOrder(SellerAppRequest receiptItems) {
        if(receiptItems.getReceiptItems() != null){
            Instant instant = Instant.now();
            long timeStampMillis = instant.toEpochMilli();
            Receipt newReceipt = new Receipt();

            newReceipt.setReceiptStatus(UNPROCESSED);
            newReceipt.setBusinessId(1L);
            newReceipt.setOfficeId(1L);
            newReceipt.setTimestamp(timeStampMillis);

            Set<ReceiptItem> items = receiptItems
                    .getReceiptItems()
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

    public void updateReceipt(Long id, ReceiptRequest receiptRequest) {
        Instant instant = Instant.now();
        long timeStampMillis = instant.toEpochMilli();
        Receipt receipt = receiptRepository.getOne(id);
        receipt.setReceiptStatus(PAYED);
        receipt.setCashRegisterId(receiptRequest.getCashRegisterId());
        receipt.setUsername(receiptRequest.getUsername());
        receipt.setTimestamp(timeStampMillis);
        receiptRepository.save(receipt);
    }

    public  BigDecimal getTotalPrice(Set<ReceiptItem> items){
        BigDecimal sum = BigDecimal.valueOf(0);
        for(ReceiptItem item: items){
            Product product = productRepository
                    .findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("No such product!"));
            BigDecimal newPrice = product.getPrice();
            BigDecimal discount = newPrice.multiply(BigDecimal.valueOf(product.getDiscount()/100.));
            System.out.println("Ovdje1:");
            System.out.println(discount);
            newPrice=newPrice.subtract(discount);
            System.out.println("Ovdje:");
            System.out.println(newPrice);
            sum = sum.add(newPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return sum;
    }

}
