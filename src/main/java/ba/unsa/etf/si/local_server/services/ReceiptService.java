package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.AppException;
import ba.unsa.etf.si.local_server.exceptions.BadRequestException;
import ba.unsa.etf.si.local_server.exceptions.CashRegisterClosedException;
import ba.unsa.etf.si.local_server.exceptions.ResourceNotFoundException;
import ba.unsa.etf.si.local_server.models.Business;
import ba.unsa.etf.si.local_server.models.transactions.*;
import ba.unsa.etf.si.local_server.repositories.ReceiptItemRepository;
import ba.unsa.etf.si.local_server.requests.EditOrderRequest;
import ba.unsa.etf.si.local_server.requests.GuestOrderRequest;
import ba.unsa.etf.si.local_server.responses.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ba.unsa.etf.si.local_server.models.Product;
import ba.unsa.etf.si.local_server.repositories.ProductRepository;
import ba.unsa.etf.si.local_server.repositories.ReceiptRepository;
import ba.unsa.etf.si.local_server.requests.ReceiptRequest;
import ba.unsa.etf.si.local_server.requests.SellerAppRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus.*;

@RequiredArgsConstructor
@Service
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final ReceiptItemRepository receiptItemRepository;
    private final ProductService productService;
    private final MainReceiptService mainReceiptService;
    private final CashRegisterService cashRegisterService;
    private final BusinessService businessService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    public ResponseEntity<Object> deleteReceipt(Long id){
        Optional<Receipt> receipt = receiptRepository.findById(id);

        if(receipt.isPresent()) {
            receiptRepository.deleteById(id);
            return new ResponseEntity<>(new Response("Receipt is successfully deleted!"), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(new Response("Already processed request!"), HttpStatus.BAD_REQUEST);
    }

    public Set<SellerAppReceiptsResponse> getSellerReceipts() {
        Set<Receipt> receipts = receiptRepository.findReceiptByReceiptStatus(UNPROCESSED);

        return receipts
                .stream()
                .map(r -> new SellerAppReceiptsResponse(r.getId(), r.getReceiptItems()
                        .stream()
                        .map(receiptItem -> new SellerAppReceiptItemsResponse(receiptItem.getProductId(), receiptItem.getQuantity()))
                        .collect(Collectors.toSet())))
                .collect(Collectors.toSet());
    }

    public String checkRequest(ReceiptRequest receiptRequest) {
        Receipt receipt;

        if(receiptRequest.getId() == null) {
            receipt = makeReceipt(receiptRequest);
        } else {
            receipt = getReceiptById(receiptRequest.getId());

            if(receipt.getReceiptStatus() != UNPROCESSED) {
                return "Already processed request!";
            }
        }

        PaymentMethod paymentMethod;

        try {
            paymentMethod = Enum.valueOf(PaymentMethod.class, receiptRequest.getPaymentMethod());
        } catch (NullPointerException | IllegalArgumentException err) {
            throw new BadRequestException("Illegal payment method");
        }

        ReceiptStatus receiptStatus = paymentMethod == PaymentMethod.PAY_APP ? PENDING : PAID;

        String[] receiptIdData = receiptRequest.getReceiptId().split("-");
        Long timestamp = Long.parseLong(receiptIdData[3]);

        Business currentBusiness = businessService.getCurrentBusiness();

        receipt.setReceiptId(receiptRequest.getReceiptId());
        receipt.setCashRegisterId(receiptRequest.getCashRegisterId());
        receipt.setOfficeId(currentBusiness.getOfficeId());
        receipt.setBusinessId(currentBusiness.getBusinessId());
        receipt.setUsername(receiptRequest.getUsername());
        receipt.setReceiptStatus(receiptStatus);
        receipt.setPaymentMethod(paymentMethod);
        receipt.setTimestamp(timestamp);
        receipt.setTotalPrice(getTotalPrice(receipt.getReceiptItems()));

        receipt.getReceiptItems().forEach(receiptItem ->
                productService.updateProductQuantity(receiptItem.getProductId(), -1 * receiptItem.getQuantity())
        );

        receiptRepository.save(receipt);
        mainReceiptService.postReceiptToMain(receipt);

        return "Successfully created receipt";
    }

    private Receipt makeReceipt(ReceiptRequest receiptRequest) {
        Set<ReceiptItem> items = receiptRequest
                .getReceiptItems()
                .stream()
                .map(receiptItemRequest -> new ReceiptItem(
                        null,
                        receiptItemRequest.getId(),
                        receiptItemRequest.getQuantity()))
                .collect(Collectors.toSet());

        Receipt receipt = new Receipt();
        receipt.setReceiptItems(items);
        return receipt;
    }

    public Receipt getReceiptById(Long id) {
        return receiptRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No receipt with ID " + id));
    }

    public String reverseReceipt(String id) {
        Receipt receipt = getReceipt(id);

        if (receipt.getReceiptStatus() == DELETED) {
            return "Cannot reverse receipt reversion";
        }

        receipt.setReceiptStatus(DELETED);
        receiptRepository.saveAndFlush(receipt);

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
                new Date().getTime(),
                null,
                receipt.isServed(),
                receipt.isSeen()
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

    public List<Receipt> getReceipts(Long cashRegisterId) {
        return receiptRepository
                .findByCashRegisterId(cashRegisterId)
                .stream()
                .filter(receipt -> receipt.getReceiptStatus() != DELETED)
                .collect(Collectors.toList());
    }

    public String saveOrder(SellerAppRequest sellerAppRequest) {
        if(sellerAppRequest.getReceiptItems() != null){
            Receipt newReceipt = new Receipt();

            if(!sellerAppRequest.getMessage().equals("")){
                newReceipt.setReceiptStatus(GUEST_ORDER);
                newReceipt.setMessage(sellerAppRequest.getMessage());
            }
            else {
                newReceipt.setReceiptStatus(UNPROCESSED);
            }

            newReceipt.setCashRegisterId(-1L);
            newReceipt.setServed(sellerAppRequest.isServed());
            newReceipt.setSeen(sellerAppRequest.isSeen());

            Set<ReceiptItem> items = sellerAppRequest
                    .getReceiptItems()
                    .stream()
                    .map(receiptItemRequest -> new ReceiptItem(null, receiptItemRequest.getId(), receiptItemRequest.getQuantity()))
                    .collect(Collectors.toSet());
            newReceipt.setReceiptItems(items);
            receiptRepository.save(newReceipt);

            if(newReceipt.getReceiptStatus().equals(GUEST_ORDER)) {
                System.out.println("GUEST ORDER");
                simpMessagingTemplate.convertAndSend("/topic/guest_order", "A guest has placed an order!");
            }

            return  "Order is successfully saved!";
        }
        return "";
    }

    public BigDecimal getTotalPrice(Set<ReceiptItem> items){
        BigDecimal sum = BigDecimal.valueOf(0);
        for(ReceiptItem item: items){
            Product product = productService.getProduct(item.getProductId());
            BigDecimal newPrice = product.getPrice();
            BigDecimal discount = newPrice.multiply(BigDecimal.valueOf(product.getDiscount()/100.));
            newPrice=newPrice.subtract(discount);
            sum = sum.add(newPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return sum;
    }

    public String editOrder(EditOrderRequest editOrderRequest) {
        Optional<Receipt> order = receiptRepository.findById(editOrderRequest.getId());
        if(order.isPresent()) {
            ReceiptStatus receiptStatus = order.get().getReceiptStatus();

            if(receiptStatus != UNPROCESSED && receiptStatus!=GUEST_ORDER) {
                return "Request denied. Receipt cannot be edited!";
            }

            else if(productService.checkProducts(editOrderRequest.getReceiptItems())) {
                receiptItemRepository.deleteByReceipt(order.get().getId());
                Set<ReceiptItem> items = editOrderRequest
                        .getReceiptItems()
                        .stream()
                        .map(receiptItemRequest -> new ReceiptItem(null, receiptItemRequest.getId(), receiptItemRequest.getQuantity()))
                        .collect(Collectors.toSet());
                order.get().setReceiptItems(items);
                order.get().setServed(editOrderRequest.isServed());
                order.get().setSeen(editOrderRequest.isSeen());
                receiptItemRepository.saveAll(items);
                return "Order is successfully changed!";
            }
            else return "Request denied. Incorrect productId!";
        }
        return "Request denied. Incorrect id!";
    }

    public List<Receipt> getDailyReceipts(Long cashRegisterId) {
        return receiptRepository
                .findByCashRegisterId(cashRegisterId)
                .stream()
                .filter(receipt -> compareTimestamps(receipt.getTimestamp()))
                .collect(Collectors.toList());
    }

    private boolean compareTimestamps(Long timestamp) {
        if(timestamp==null) return false;
        Date date = new Date(timestamp);
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(currentDate).equals(sdf.format(date));
    }

    public Set<GuestOrderResponse> getGuestReceipts() {
        Set<Receipt> receipts = receiptRepository.findReceiptByReceiptStatus(GUEST_ORDER);

        return receipts
                .stream()
                .map(r -> new GuestOrderResponse(r.getId(), r.getMessage(), r.isServed(), r.isSeen(), r.getReceiptItems()
                        .stream()
                        .map(receiptItem -> new SellerAppReceiptItemsResponse(receiptItem.getProductId(), receiptItem.getQuantity()))
                        .collect(Collectors.toSet())))
                .collect(Collectors.toSet());
    }

    public String saveGuestOrder(GuestOrderRequest guestOrderRequest) {
        Optional<Receipt> order = receiptRepository.findById(guestOrderRequest.getId());
        if(order.isPresent()) {
            ReceiptStatus receiptStatus = order.get().getReceiptStatus();

            if(receiptStatus!=GUEST_ORDER) {
                return "Request denied. Order already closed!";
            }

            receiptItemRepository.deleteByReceipt(order.get().getId());
            Set<ReceiptItem> items = guestOrderRequest
                    .getReceiptItems()
                    .stream()
                    .map(receiptItemRequest -> new ReceiptItem(null, receiptItemRequest.getId(), receiptItemRequest.getQuantity()))
                    .collect(Collectors.toSet());
            order.get().setReceiptItems(items);
            receiptItemRepository.saveAll(items);
            order.get().setReceiptStatus(UNPROCESSED);
            receiptRepository.save(order.get());

            return "Order is successfully saved!";
        }
        return "Request denied. Incorrect id!";
    }
}
