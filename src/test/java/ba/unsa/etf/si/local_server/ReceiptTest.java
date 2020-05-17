package ba.unsa.etf.si.local_server;


import ba.unsa.etf.si.local_server.models.Business;
import ba.unsa.etf.si.local_server.models.CashRegister;
import ba.unsa.etf.si.local_server.models.transactions.Receipt;
import ba.unsa.etf.si.local_server.repositories.ReceiptRepository;
import ba.unsa.etf.si.local_server.repositories.TableRepository;
import ba.unsa.etf.si.local_server.responses.SellerAppInfoResponse;
import ba.unsa.etf.si.local_server.services.ReceiptService;
import ba.unsa.etf.si.local_server.services.TableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus.PAID;
import static ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus.UNPROCESSED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReceiptTest {
    @Mock
    private ReceiptRepository receiptRepository;

    @InjectMocks
    private ReceiptService receiptService;

    @Test
    void getSellerAppDataTest(){

        Receipt receipt = new Receipt();
        receipt.setReceiptStatus(UNPROCESSED);
        receipt.setServed(true);
        receipt.setSeen(true);
        receipt.setMessage("");
        receipt.setReceiptId("");
        receipt.setUsername("");
        receipt.setId(1L);

        given(receiptRepository.findById(receipt.getId())).willReturn(Optional.of(receipt));

        ResponseEntity<Object> responseEntity = receiptService.deleteReceipt(receipt.getId());

        verify(receiptRepository, times(1)).deleteById(receipt.getId());
    }

    @Test
    void getSellerAppReceiptsTest(){

        Receipt receipt = new Receipt();
        receipt.setReceiptStatus(UNPROCESSED);
        receipt.setServed(true);
        receipt.setSeen(true);
        receipt.setMessage("");
        receipt.setReceiptId("");
        receipt.setUsername("");
        receipt.setId(1L);

        given(receiptRepository.findById(receipt.getId())).willReturn(Optional.of(receipt));

        ResponseEntity<Object> responseEntity = receiptService.deleteReceipt(receipt.getId());

        verify(receiptRepository, times(1)).deleteById(receipt.getId());
    }

    @Test
    void getReceiptByIdTest(){

        Receipt receipt = new Receipt();
        receipt.setReceiptStatus(UNPROCESSED);
        receipt.setServed(true);
        receipt.setSeen(true);
        receipt.setMessage("");
        receipt.setReceiptId("");
        receipt.setUsername("");
        receipt.setId(1L);

        given(receiptRepository.findById(receipt.getId())).willReturn(Optional.of(receipt));

        Receipt receipt1 = receiptService.getReceiptById(receipt.getId());

        assertThat(receipt1).isEqualTo(receipt);
    }

    @Test
    void updateReceiptStatusTest(){

        Receipt receipt = new Receipt();
        receipt.setReceiptStatus(UNPROCESSED);
        receipt.setServed(true);
        receipt.setSeen(true);
        receipt.setMessage("");
        receipt.setReceiptId("");
        receipt.setUsername("");
        receipt.setId(1L);

        given(receiptRepository.findById(receipt.getId())).willReturn(Optional.of(receipt));
        given(receiptRepository.findByReceiptId(receipt.getReceiptId())).willReturn(Optional.of(receipt));
        given(receiptRepository.save(receipt)).willAnswer(inocation -> inocation.getArgument(0));

        receiptService.updateReceiptStatus(receipt.getReceiptId(), PAID);
        Receipt receipt1 = receiptService.getReceiptById(receipt.getId());

        assertThat(receipt1.getReceiptStatus()).isEqualTo(PAID);
    }

    @Test
    void getReceipTest(){

        Receipt receipt = new Receipt();
        receipt.setReceiptStatus(UNPROCESSED);
        receipt.setServed(true);
        receipt.setSeen(true);
        receipt.setMessage("");
        receipt.setReceiptId("");
        receipt.setUsername("");
        receipt.setId(1L);
        receipt.setCashRegisterId(1L);

        Receipt receipt1 = new Receipt();
        receipt1.setReceiptStatus(UNPROCESSED);
        receipt1.setServed(true);
        receipt1.setSeen(true);
        receipt1.setMessage("");
        receipt1.setReceiptId("");
        receipt1.setUsername("");
        receipt1.setId(1L);
        receipt1.setCashRegisterId(1L);

        Receipt receipt2 = new Receipt();
        receipt2.setReceiptStatus(UNPROCESSED);
        receipt2.setServed(true);
        receipt2.setSeen(true);
        receipt2.setMessage("");
        receipt2.setReceiptId("");
        receipt2.setUsername("");
        receipt2.setId(1L);
        receipt2.setCashRegisterId(2L);

        List<Receipt> receipts = new ArrayList<>();
        receipts.add(receipt);
        receipts.add(receipt1);

        given(receiptRepository.findByCashRegisterId(receipt.getCashRegisterId())).willReturn(receipts);

        List<Receipt> list = receiptService.getReceipts(1L);

        assertThat(list.size()).isEqualTo(2);
    }


}
