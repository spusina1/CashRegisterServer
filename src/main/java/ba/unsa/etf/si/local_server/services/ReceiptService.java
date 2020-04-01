package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.models.transactions.Receipt;
import ba.unsa.etf.si.local_server.repositories.ReceiptRepository;
import ba.unsa.etf.si.local_server.responses.DeleteReceiptResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ReceiptService {
    private final ReceiptRepository receiptRepository;


    //public List<Receipt> getReceipts() {
    //    return receiptRepository.findAll();
    //}

    public ResponseEntity<Object> deleteReceipt(Long id){
        Optional<Receipt> receipt = receiptRepository.findById(id);

        if(receipt.isPresent()) {
            receiptRepository.deleteById(id);
            return new ResponseEntity<>(new DeleteReceiptResponse("Receipt is successfully deleted!"), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(new DeleteReceiptResponse("Already processed request!"), HttpStatus.BAD_REQUEST);
    }
}
