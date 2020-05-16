package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.ResourceNotFoundException;
import ba.unsa.etf.si.local_server.models.Business;
import ba.unsa.etf.si.local_server.models.CashRegister;
import ba.unsa.etf.si.local_server.repositories.BusinessRepository;
import ba.unsa.etf.si.local_server.repositories.CashRegisterRepository;
import ba.unsa.etf.si.local_server.responses.CashRegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CashRegisterService {
    private final CashRegisterRepository cashRegisterRepository;
    private final BusinessService businessService;

    public CashRegisterResponse registerCashRegister() {
        CashRegister cashRegister = cashRegisterRepository
                .findDistinctFirstByTakenIsFalse()
                .orElseThrow(() -> new ResourceNotFoundException("No available cash registers!"));

        cashRegister.setTaken(true);
        cashRegisterRepository.save(cashRegister);

        return makeCashRegisterResponse(cashRegister);
    }

    public void batchInsertCashRegisters(List<CashRegister> cashRegisters) {
        cashRegisters.forEach(cashRegister ->
                cashRegisterRepository.saveCashRegister(cashRegister.getId(), cashRegister.getName(), cashRegister.getUuid())
        );
        cashRegisterRepository.flush();
    }

    public void openRegisters() {
        System.out.println("Opening cash registers...");
        List<CashRegister> cashRegisters = cashRegisterRepository.findAll();

        for(CashRegister cashRegister:cashRegisters) {
            cashRegister.setOpen(true);
            cashRegisterRepository.save(cashRegister);
        }
    }

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
        return makeCashRegisterResponse(cashRegister);
    }

    private CashRegisterResponse makeCashRegisterResponse(CashRegister cashRegister) {
        Business business = businessService.getCurrentBusiness();
        return new CashRegisterResponse(
                cashRegister.getId(),
                cashRegister.getName(),
                business.getOfficeId(),
                business.getId(),
                business.getBusinessName(),
                cashRegister.getUuid(),
                business.getStartTime(),
                business.getEndTime(),
                business.getLanguage(),
                business.isRestaurant(),
                business.getPlaceName());
    }

    public void setIsCashRegisterOpen(boolean cashRegisterOpen) {
        List<CashRegister> cashRegisters = cashRegisterRepository.findAll();
        cashRegisters.forEach(cashRegister -> cashRegister.setOpen(cashRegisterOpen));
        cashRegisterRepository.saveAll(cashRegisters);
        cashRegisterRepository.flush();
    }
}
