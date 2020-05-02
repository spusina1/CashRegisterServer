package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.models.Business;
import ba.unsa.etf.si.local_server.models.CashRegister;
import ba.unsa.etf.si.local_server.repositories.BusinessRepository;
import ba.unsa.etf.si.local_server.repositories.CashRegisterRepository;
import ba.unsa.etf.si.local_server.repositories.ProductRepository;
import ba.unsa.etf.si.local_server.responses.SellerAppInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BusinessService {
    private final BusinessRepository businessRepository;
    private final CashRegisterRepository cashRegisterRepository;

    @Value("${main_server.office_id}")
    private long officeId;

    @Value("${main_server.business_id}")
    private long businessId;

 public void updateBussinesInfo(Business business){
     cashRegisterRepository.deleteAllInBatch();
     cashRegisterRepository.flush();
     businessRepository.deleteAllInBatch();
     businessRepository.flush();
     businessRepository.save(business);
     businessRepository.flush();
     cashRegisterRepository.flush();
 }

    public SellerAppInfoResponse getSellerAppData() {
        SellerAppInfoResponse sellerAppInfoResponse = new SellerAppInfoResponse();
        Business business = businessRepository.findAll().get(0);
        sellerAppInfoResponse.setBusinessName(business.getBusinessName());
        sellerAppInfoResponse.setLanguage(business.getLanguage());
        sellerAppInfoResponse.setRestaurant(business.isRestaurant());
        sellerAppInfoResponse.setBusinessId(businessId);
        sellerAppInfoResponse.setOfficeId(officeId);
        return sellerAppInfoResponse;
    }
}
