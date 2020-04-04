package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.BadRequestException;
import ba.unsa.etf.si.local_server.exceptions.ResourceNotFoundException;
import ba.unsa.etf.si.local_server.models.transactions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import ba.unsa.etf.si.local_server.responses.SellerAppReceiptItemsResponse;
import ba.unsa.etf.si.local_server.responses.SellerAppReceiptsResponse;
import ba.unsa.etf.si.local_server.responses.DeleteReceiptResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ba.unsa.etf.si.local_server.models.Product;
import ba.unsa.etf.si.local_server.repositories.ProductRepository;
import ba.unsa.etf.si.local_server.repositories.ReceiptRepository;
import ba.unsa.etf.si.local_server.requests.ReceiptRequest;
import ba.unsa.etf.si.local_server.requests.SellerAppRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.*;
import java.time.Instant;

import static ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus.*;

@RequiredArgsConstructor
@Service
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final ProductService productService;
    private final MainReceiptService mainReceiptService;


    @Value("${main_server.office_id}")
    private long officeId;

    @Value("${main_server.business_id}")
    private long businessId;

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
        Set<ReceiptItem> items = receiptRequest
                    .getReceiptItems()
                    .stream()
                    .map(receiptItemRequest -> new ReceiptItem(
                            null,
                            receiptItemRequest.getId(),
                            receiptItemRequest.getQuantity()))
                    .collect(Collectors.toSet());

        String[] receiptIdData = receiptRequest.getReceiptId().split("-");
        Long now = Long.parseLong(receiptIdData[3]);

        PaymentMethod paymentMethod;

        try {
            paymentMethod = Enum.valueOf(PaymentMethod.class, receiptRequest.getPaymentMethod());
        } catch (NullPointerException | IllegalArgumentException err) {
            throw new BadRequestException("Illegal payment method");
        }

        ReceiptStatus receiptStatus = paymentMethod == PaymentMethod.PAY_APP ? PENDING : PAID;

        Receipt receipt = new Receipt(
                null,
                receiptRequest.getReceiptId(),
                receiptStatus,
                paymentMethod,
                receiptRequest.getCashRegisterId(),
                officeId,
                businessId,
                items,
                receiptRequest.getUsername(),
                getTotalPrice(items),
                now
        );

        items.forEach(receiptItem ->
            productService.updateProductQuantity(receiptItem.getProductId(), -1 * receiptItem.getQuantity())
        );


        receiptRepository.save(receipt);
        mainReceiptService.postReceiptToMain(receipt);

        if(receipt.getReceiptStatus() == PENDING) {
            mainReceiptService.pollReceiptStatus(receipt.getReceiptId());
        }

        return "Successfully created receipt";
    }

    public String reverseReceipt(String id) {
        Receipt receipt = getReceipt(id);

        if (receipt.getReceiptStatus() == DELETED) {
            return "Cannot reverse receipt reversion";
        }

        Receipt reversedReceipt = getReversedReceipt(receipt);

        reversedReceipt
                .getReceiptItems()
                .forEach(receiptItem ->
                        productService.updateProductQuantity(receiptItem.getProductId(), -1 * receiptItem.getQuantity())
                );

        try {
            receiptRepository.save(reversedReceipt);
            mainReceiptService.postReceiptToMain(reversedReceipt);
        } catch (ConstraintViolationException err) {
            return "Cannot reverse already reversed receipt";
        }

        return "Receipt successfully reversed!";
    }

    private Receipt getReversedReceipt(Receipt receipt) {
        BigDecimal reversedTotalPrice = receipt.getTotalPrice().multiply(BigDecimal.valueOf(-1));

        Set<ReceiptItem> items = receipt
                .getReceiptItems()
                .stream()
                .map(item -> new ReceiptItem(null, item.getProductId(), item.getQuantity() * -1))
                .collect(Collectors.toSet());

        return new Receipt(
                null,
                "-" + receipt.getReceiptId(),
                DELETED,
                receipt.getPaymentMethod(),
                receipt.getCashRegisterId(),
                receipt.getOfficeId(),
                receipt.getBusinessId(),
                items,
                receipt.getUsername(),
                reversedTotalPrice,
                new Date().getTime()
        );
    }

    public void updateReceiptStatus(String id, ReceiptStatus receiptStatus) {
        Receipt receipt = getReceipt(id);
        receipt.setReceiptStatus(receiptStatus);
        receiptRepository.save(receipt);
    }

    public Receipt getReceipt(String id) {
        return receiptRepository
                .findByReceiptId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such receipt!"));
    }

    private BigDecimal getTotalPrice(Set<ReceiptItem> items){
        return items
                .stream()
                .map(item ->
                        productService
                                .getProduct(item.getProductId())
                                .getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Receipt> getReceipts(Long cashRegisterId) {
        return receiptRepository.findByCashRegisterId(cashRegisterId);
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
