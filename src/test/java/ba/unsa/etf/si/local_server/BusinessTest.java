package ba.unsa.etf.si.local_server;

import ba.unsa.etf.si.local_server.models.Business;
import ba.unsa.etf.si.local_server.models.CashRegister;
import ba.unsa.etf.si.local_server.models.Item;
import ba.unsa.etf.si.local_server.repositories.BusinessRepository;
import ba.unsa.etf.si.local_server.repositories.CashRegisterRepository;
import ba.unsa.etf.si.local_server.repositories.ItemRepository;
import ba.unsa.etf.si.local_server.responses.SellerAppInfoResponse;
import ba.unsa.etf.si.local_server.services.BusinessService;
import ba.unsa.etf.si.local_server.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.Modifying;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BusinessTest {

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private CashRegisterRepository cashRegisterRepository;

    @InjectMocks
    private BusinessService businessService;

    @Test
    void updateBusinessInfoTest(){

        CashRegister cashRegister = new CashRegister();
        cashRegister.setName("cashRegister1");
        cashRegister.setOpen(true);
        cashRegister.setTaken(true);
        cashRegister.setId(1L);
        cashRegister.setUuid("");

        List<CashRegister> cashRegisters = new ArrayList<>();
        cashRegisters.add(cashRegister);

        Business business = new Business();
        business.setPlaceName("placeName");
        business.setCashRegisters(cashRegisters);
        business.setBusinessName("businessName");
        business.setLanguage("bs");
        business.setRestaurant(true);
        business.setOfficeId(1L);
        business.setBusinessId(1L);
        business.setId(1L);


        given(businessRepository.save(business)).willAnswer(inocation -> inocation.getArgument(0));

       businessService.updateBusinessInfo(business);

       verify(cashRegisterRepository, times(1)).deleteAllInBatch();
       verify(cashRegisterRepository, times(2)).flush();
       verify(businessRepository, times(1)).deleteAllInBatch();
       verify(businessRepository, times(2)).flush();

    }


    @Test
    void getCurrentBusinessTest(){

        CashRegister cashRegister = new CashRegister();
        cashRegister.setName("cashRegister1");
        cashRegister.setOpen(true);
        cashRegister.setTaken(true);
        cashRegister.setId(1L);
        cashRegister.setUuid("");

        List<CashRegister> cashRegisters = new ArrayList<>();
        cashRegisters.add(cashRegister);

        Business business = new Business();
        business.setPlaceName("placeName");
        business.setCashRegisters(cashRegisters);
        business.setBusinessName("businessName");
        business.setLanguage("bs");
        business.setRestaurant(true);
        business.setOfficeId(1L);
        business.setBusinessId(1L);
        business.setId(1L);


        given(businessRepository.findById(business.getId())).willReturn(Optional.of(business));

        Business business1 = businessService.getCurrentBusiness();

        assertThat(business1.getBusinessName()).isEqualTo(business.getBusinessName());
    }

    @Test
    void getSellerAppDataTest(){

        CashRegister cashRegister = new CashRegister();
        cashRegister.setName("cashRegister1");
        cashRegister.setOpen(true);
        cashRegister.setTaken(true);
        cashRegister.setId(1L);
        cashRegister.setUuid("");

        List<CashRegister> cashRegisters = new ArrayList<>();
        cashRegisters.add(cashRegister);

        Business business = new Business();
        business.setPlaceName("placeName");
        business.setCashRegisters(cashRegisters);
        business.setBusinessName("businessName");
        business.setLanguage("bs");
        business.setRestaurant(true);
        business.setOfficeId(1L);
        business.setBusinessId(1L);
        business.setId(1L);

        List<Business> businessList = new ArrayList<>();
        businessList.add(business);


        given(businessRepository.findById(business.getId())).willReturn(Optional.of(business));
        given(businessRepository.findAll()).willReturn(businessList);

        Business business1 = businessService.getCurrentBusiness();
        SellerAppInfoResponse sellerAppInfoResponse = businessService.getSellerAppData();

        assertThat(sellerAppInfoResponse.getPlaceName()).isEqualTo(business.getPlaceName());
    }



}
