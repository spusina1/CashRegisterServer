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

    public void updateBusinessInfo(Business business){
        cashRegisterRepository.deleteAllInBatch();
        cashRegisterRepository.flush();
        businessRepository.deleteAllInBatch();
        businessRepository.flush();
        businessRepository.save(business);
        businessRepository.flush();
        cashRegisterRepository.flush();
    }

    public Business getCurrentBusiness() {
        return businessRepository.findById(1L).orElse(null);
    }

    public SellerAppInfoResponse getSellerAppData() {
        Business currentBusiness = getCurrentBusiness();

        SellerAppInfoResponse sellerAppInfoResponse = new SellerAppInfoResponse();
        Business business = businessRepository.findAll().get(0);
        sellerAppInfoResponse.setBusinessName(business.getBusinessName());
        sellerAppInfoResponse.setLanguage(business.getLanguage());
        sellerAppInfoResponse.setRestaurant(business.isRestaurant());
        sellerAppInfoResponse.setBusinessId(currentBusiness.getId());
        sellerAppInfoResponse.setOfficeId(currentBusiness.getOfficeId());
        sellerAppInfoResponse.setPlaceName(business.getPlaceName());
        return sellerAppInfoResponse;
    }
}
