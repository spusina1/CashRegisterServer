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

}
