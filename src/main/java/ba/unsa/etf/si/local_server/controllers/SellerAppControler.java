package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.responses.CashRegisterResponse;
import ba.unsa.etf.si.local_server.responses.SellerAppInfoResponse;
import ba.unsa.etf.si.local_server.services.CashRegisterService;
import ba.unsa.etf.si.local_server.services.SellerAppService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class SellerAppControler {

    private final SellerAppService sellerAppService;

    @Secured({"ROLE_OFFICEMAN", "ROLE_BARTENDER", "ROLE_GUEST"})
    @GetMapping("/api/seller-app/data")
    public ResponseEntity<SellerAppInfoResponse> obtainSellerAppData() {
        SellerAppInfoResponse response = sellerAppService.getData();
        return ResponseEntity.ok(response);
    }
}
