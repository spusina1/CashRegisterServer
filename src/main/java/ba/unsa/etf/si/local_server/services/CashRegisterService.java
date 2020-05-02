package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.ResourceNotFoundException;
import ba.unsa.etf.si.local_server.models.Business;
import ba.unsa.etf.si.local_server.models.CashRegister;
import ba.unsa.etf.si.local_server.repositories.BusinessRepository;
import ba.unsa.etf.si.local_server.repositories.CashRegisterRepository;
import ba.unsa.etf.si.local_server.responses.CashRegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CashRegisterService {
    private final CashRegisterRepository cashRegisterRepository;
    private final BusinessRepository businessRepository;

    @Value("${main_server.office_id}")
    private long officeID;

    @Value("${main_server.business_id}")
    private long businessID;

    private String businessName = "";

    public CashRegisterResponse registerCashRegister() {
        CashRegister cashRegister = cashRegisterRepository
                .findDistinctFirstByTakenIsFalse()
                .orElseThrow(() -> new ResourceNotFoundException("No available cash registers!"));

        cashRegister.setTaken(true);
        cashRegisterRepository.save(cashRegister);
        Business business = businessRepository.findAll().get(0);

        return makeCashRegisterResponse(cashRegister, business);
    }

    public void batchInsertCashRegisters(List<CashRegister> cashRegisters) {
        cashRegisters.forEach(cashRegister ->
                cashRegisterRepository.saveCashRegister(cashRegister.getId(), cashRegister.getName(), cashRegister.getUuid())
        );
        cashRegisterRepository.flush();
    }

    public void updateBusinessName(String businessName) {
        this.businessName = businessName;
    }

//    @Scheduled(cron = "${cron.open}")
    public void openRegisters() {
        System.out.println("Opening cash registers...");
        List<CashRegister> cashRegisters = cashRegisterRepository.findAll();

        for(CashRegister cashRegister:cashRegisters) {
            cashRegister.setOpen(true);
            cashRegisterRepository.save(cashRegister);
        }
    }

//    @Scheduled(cron = "${cron.close}")
    public void closeRegisters() {
        System.out.println("Closing cash registers...");
        List<CashRegister> cashRegisters = cashRegisterRepository.findAll();

        for(CashRegister cashRegister:cashRegisters) {
            cashRegister.setOpen(false);
            cashRegisterRepository.save(cashRegister);
        }
    }

    public boolean isCashRegisterOpen(Long id) {
        CashRegister cashRegister = cashRegisterRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No cash registers with id " + id +"!"));
        return cashRegister.isOpen();
    }

    public CashRegisterResponse getCashRegisterData(Long id) {
        CashRegister cashRegister = cashRegisterRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No available cash registers!"));
        Business business = businessRepository.findAll().get(0);
        return makeCashRegisterResponse(cashRegister, business);
    }

    private CashRegisterResponse makeCashRegisterResponse(CashRegister cashRegister, Business business) {
        return new CashRegisterResponse(
                cashRegister.getId(),
                cashRegister.getName(),
                officeID,
                businessID,
                business.getBusinessName(),
                cashRegister.getUuid(),
                business.getStartTime(),
                business.getEndTime(),
                business.getLanguage(),
                business.isRestaurant());
    }

}
