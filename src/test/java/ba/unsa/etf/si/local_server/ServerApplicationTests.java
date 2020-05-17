package ba.unsa.etf.si.local_server;

import ba.unsa.etf.si.local_server.models.*;
import ba.unsa.etf.si.local_server.repositories.ProductRepository;
import ba.unsa.etf.si.local_server.repositories.RoleRepository;
import ba.unsa.etf.si.local_server.repositories.UserRepository;
import ba.unsa.etf.si.local_server.requests.ReceiptRequest;
import ba.unsa.etf.si.local_server.services.*;
import org.apache.catalina.core.ApplicationContext;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest
@RunWith(SpringRunner.class)
class ServerApplicationTests {
    @Autowired
    private ReceiptService receiptService;
    @Autowired
    private MainSyncUpService mainSyncUpService;
    @Autowired
    private BusinessService businessService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @MockBean
    private ProductRepository productRepository;

    @Test
    void testTest() {
        Mockito.when(productRepository.findAll()).thenReturn(Arrays.asList(new Product(), new Product()));

        for(Product p : productRepository.findAll()) {
            System.out.println("Yay");
        }
    }

    @Test
    void mockTest() {
        Mockito.when(productRepository.count()).thenReturn(123L);
        long count = productRepository.count();
        System.out.println(count);
        Assert.assertEquals(123L, count);
        Mockito.verify(productRepository).count();
    }

}
