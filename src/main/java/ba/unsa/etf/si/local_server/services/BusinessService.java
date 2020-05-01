package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.models.Business;
import ba.unsa.etf.si.local_server.models.CashRegister;
import ba.unsa.etf.si.local_server.repositories.BusinessRepository;
import ba.unsa.etf.si.local_server.repositories.CashRegisterRepository;
import ba.unsa.etf.si.local_server.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BusinessService {
    private final BusinessRepository businessRepository;
    private final CashRegisterRepository cashRegisterRepository;

 public void updateBussinesInfo(Business business){
     cashRegisterRepository.deleteAllInBatch();
     cashRegisterRepository.flush();
     businessRepository.deleteAllInBatch();
     businessRepository.flush();
     businessRepository.save(business);
     businessRepository.flush();
     cashRegisterRepository.flush();
 }
}
