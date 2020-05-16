package ba.unsa.etf.si.local_server;

import ba.unsa.etf.si.local_server.models.*;
import ba.unsa.etf.si.local_server.models.transactions.ReceiptItem;
import ba.unsa.etf.si.local_server.repositories.ProductRepository;
import ba.unsa.etf.si.local_server.repositories.RoleRepository;
import ba.unsa.etf.si.local_server.repositories.UserRepository;
import ba.unsa.etf.si.local_server.requests.ReceiptItemRequest;
import ba.unsa.etf.si.local_server.requests.ReceiptRequest;
import ba.unsa.etf.si.local_server.services.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.Example;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
class ServerApplicationTests {
    private final ReceiptService receiptService;
    private final MainSyncUpService mainSyncUpService;
    private final BusinessService businessService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProductRepository productRepository;

    @Test
    void contextLoads() {
    }

    @BeforeAll
    void syncDatabase() {
        mainSyncUpService.syncDatabases();
    }

    @Test
    void createNewReceiptCashPayment() {
        Business business = businessService.getCurrentBusiness();

        List<Product> products = productRepository.findAll();
        Product someProduct = products
                .stream()
                .filter(product -> product.getQuantity() > 0)
                .findFirst()
                .orElseThrow(
                        () -> new IllegalStateException("No products in storege")
                );

        Set<ReceiptItemRequest> receiptItems = new HashSet<>();
        receiptItems.add(new ReceiptItemRequest(someProduct.getId(), 1D));

        // TODO: Ovdje timestamp mozda ne valja
        Long timestamp = Timestamp.valueOf(LocalDateTime.now()).getTime();
        System.out.println("This is a timestamp: " + timestamp);

        CashRegister cashRegister = business
                .getCashRegisters()
                .stream()
                .findFirst()
                .orElseThrow(
                    () -> new IllegalStateException("No cash registers in office! Can't make receipt")
                );

        String receiptId = String.format(
                "%d-%d-%d-%d",
                business.getBusinessId(),
                business.getOfficeId(),
                cashRegister.getId(),
                timestamp
        );

        Role roleBartender = roleRepository
                .findByRolename(RoleName.ROLE_CASHIER)
                .orElseThrow(
                        () -> new IllegalStateException("No cashier in office!")
                );

        User user = userRepository
                .findAll()
                .stream()
                .filter(u -> u.getRoles().contains(roleBartender))
                .findFirst().orElseThrow(
                () -> new IllegalStateException("No cashier in office!")
        );

        ReceiptRequest receiptRequest = new ReceiptRequest(
                0L,
                receiptId,
                user.getUsername(),
                cashRegister.getId(),
                "CASH",
                receiptItems
        );

        receiptService.checkRequest(receiptRequest);
    }

}
