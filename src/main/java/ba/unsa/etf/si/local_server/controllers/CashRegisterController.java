package ba.unsa.etf.si.local_server.controllers;

import ba.unsa.etf.si.local_server.services.CashRegisterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class CashRegisterController {
    private final CashRegisterService cashRegisterService;

    @PostMapping("/api/open")
    public ResponseEntity<?> openRegister(@PathVariable Long id){
        String responseMessage = cashRegisterService.openRegister(id);
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping("/api/close")
    public ResponseEntity<?> closeRegister(@PathVariable Long id){
        String responseMessage = cashRegisterService.closeRegister(id);
        return ResponseEntity.ok(responseMessage);
    }
}
