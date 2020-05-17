package ba.unsa.etf.si.local_server;

import ba.unsa.etf.si.local_server.models.Item;
import ba.unsa.etf.si.local_server.repositories.ItemRepository;
import ba.unsa.etf.si.local_server.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ItemTest {
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ProductService productService;


    @Test
    void getItemsTest(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Salt");
        item.setUnit("kg");
        List<Item> items = new ArrayList<Item>();
        items.add(item);

        given(itemRepository.findAll()).willReturn(items);

        List<Item> list = productService.getItems();

        assertThat(list).isEqualTo(items);
    }

}
