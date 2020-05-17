package ba.unsa.etf.si.local_server;

import ba.unsa.etf.si.local_server.models.Item;
import ba.unsa.etf.si.local_server.models.Table;
import ba.unsa.etf.si.local_server.models.User;
import ba.unsa.etf.si.local_server.repositories.ItemRepository;
import ba.unsa.etf.si.local_server.repositories.TableRepository;
import ba.unsa.etf.si.local_server.repositories.UserRepository;
import ba.unsa.etf.si.local_server.services.ProductService;
import ba.unsa.etf.si.local_server.services.TableService;
import ba.unsa.etf.si.local_server.services.UserService;
import org.assertj.core.api.AssertionsForClassTypes;
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
public class TableTest {

    @Mock
    private TableRepository tableRepository;

    @InjectMocks
    private TableService tableService;


    @Test
    void getTablesTest(){

        Table table = new Table();
        table.setId(1L);
        table.setTableName("table1");

        List<Table> list = new ArrayList<>();
        list.add(table);

        given(tableRepository.findAll()).willReturn(list);

        List<Table> tables = tableService.getTables();

        assertThat(tables).isEqualTo(list);
    }
}
