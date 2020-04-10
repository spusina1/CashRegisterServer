package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.responses.CashRegisterResponse;
import ba.unsa.etf.si.local_server.services.CashRegisterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class CashRegisterController {
    private final CashRegisterService cashRegisterService;

    @PostMapping("/api/open")
    public ResponseEntity<?> openRegister(@RequestParam(required = false, name = "cash_register_id") Long id){
        String responseMessage = cashRegisterService.openRegister(id);
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping("/api/close")
    public ResponseEntity<?> closeRegister(@RequestParam(required = false, name = "cash_register_id") Long id){
        String responseMessage = cashRegisterService.closeRegister(id);
        return ResponseEntity.ok(responseMessage);
    }

    @Secured("ROLE_OFFICEMAN")
    @PostMapping("/api/register")
    public ResponseEntity<?> obtainIds() {
        CashRegisterResponse response = cashRegisterService.registerCashRegister();
        return ResponseEntity.ok(response);
    }

}
