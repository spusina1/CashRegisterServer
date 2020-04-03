package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.BadRequestException;
import ba.unsa.etf.si.local_server.exceptions.ResourceNotFoundException;
import ba.unsa.etf.si.local_server.models.Product;
import ba.unsa.etf.si.local_server.models.transactions.*;
import ba.unsa.etf.si.local_server.repositories.ProductRepository;
import ba.unsa.etf.si.local_server.repositories.ReceiptRepository;
import ba.unsa.etf.si.local_server.requests.ReceiptRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus.*;

@RequiredArgsConstructor
@Service
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final ProductService productService;

    @Value("${main_server.office_id}")
    private long officeId;

    @Value("${main_server.business_id}")
    private long businessId;

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

        ReceiptStatus receiptStatus = paymentMethod == PaymentMethod.PAY_APP ? PENDING : PAYED;

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

        System.out.println(receipt);

        try {
            receiptRepository.save(receipt);
        } catch (ConstraintViolationException err) {
            String message = String.format("Receipt with ID %s already exists", receiptRequest.getReceiptId());
            throw new BadRequestException(message);
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

    public void updateReceipt(String id) {
        Receipt receipt = getReceipt(id);
        receipt.setReceiptStatus(PAYED);
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
}
