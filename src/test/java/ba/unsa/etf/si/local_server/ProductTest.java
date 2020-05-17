package ba.unsa.etf.si.local_server;

import ba.unsa.etf.si.local_server.models.Product;
import ba.unsa.etf.si.local_server.repositories.ProductRepository;
import ba.unsa.etf.si.local_server.requests.FilterRequest;
import ba.unsa.etf.si.local_server.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;


    @Test
    void getProductsTest(){
        Product product = new Product();
        product.setName("Bread");
        product.setMeasurementUnit("kg");
        List<Product> productList = new ArrayList<Product>();
        productList.add(product);

        given(productRepository.findByFilter(new FilterRequest())).willReturn(productList);

        List<Product> list = productService.getProducts(new FilterRequest());

        assertThat(list).isEqualTo(productList);
    }


    @Test
    void updateProductQuantityTest(){
        Product product = new Product();
        product.setId(1L);
        product.setName("Bread");
        product.setMeasurementUnit("kg");
        product.setQuantity(0.);

        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
        given(productRepository.save(product)).willAnswer(inocation -> inocation.getArgument(0));


        productService.updateProductQuantity(product.getId(), 2.);
        Product product1 = productService.getProduct(product.getId());

        assertThat(product1.getQuantity()).isEqualTo(2.);
    }


}
