package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.responses.SellerAppInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SellerAppService {

    private final BusinessService businessService;


    public SellerAppInfoResponse getData() {
        return businessService.getSellerAppData();
    }
}
