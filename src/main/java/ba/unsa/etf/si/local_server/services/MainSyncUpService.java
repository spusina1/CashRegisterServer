package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.AppException;
import ba.unsa.etf.si.local_server.models.CashRegister;
import ba.unsa.etf.si.local_server.models.Product;
import ba.unsa.etf.si.local_server.models.Table;
import ba.unsa.etf.si.local_server.models.User;
import ba.unsa.etf.si.local_server.repositories.CashRegisterRepository;
import ba.unsa.etf.si.local_server.responses.CashRegisterResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class MainSyncUpService {
    private final HttpClientService httpClientService;
    private final UserService userService;
    private final ProductService productService;
    private final CashRegisterService cashRegisterService;
    private final TableService tableService;

    @Value("${main_server.office_id}")
    private long officeID;

    @Value("${main_server.business_id}")
    private long businessID;

    @Scheduled(cron = "${cron.main_fetch}")
    public void syncDatabases() {
        System.out.println("Synchronizing databases...");
        List<User> users = fetchUsersFromMain();
        List<Product> products = fetchProductsFromMain();
        List<CashRegister> cashRegisters = fetchCashRegistersFromMain();
        List<Table> tables = fetchTablesFromMain();

        userService.batchInsertUsers(users);
        productService.batchInsertProducts(products);
        cashRegisterService.batchInsertCashRegisters(cashRegisters);
        tableService.batchInsertTables(tables);
        System.out.println("Yaaay, Synchronisation complete!");
    }

    private List<Product> fetchProductsFromMain() {
        String uri = String.format("/offices/%d/products", officeID);
        String json = httpClientService.makeGetRequest(uri);
        return jsonListToObjectList(json, this::mapJsonToProduct);
    }

    private List<User> fetchUsersFromMain() {
        String uri = "/office-employees";
        String json = httpClientService.makeGetRequest(uri);
        return jsonListToObjectList(json, this::mapJsonToUser);
    }

    private List<CashRegister> fetchCashRegistersFromMain() {
        String uri = String.format("/business/%d/office-details/%d", businessID, officeID);
        String json = httpClientService.makeGetRequest(uri);
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonArray;

        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            jsonArray = jsonNode.get("cashRegisters").toString();

            String businessName = jsonNode.get("businessName").asText();
            cashRegisterService.updateBusinessName(businessName);
        } catch (JsonProcessingException e) {
            throw new AppException("Expected json response");
        }

        return jsonListToObjectList(jsonArray, this::mapJsonToCashRegister);
    }

    private <T> ArrayList<T> jsonListToObjectList(String json, Function<JsonNode, T> mapJsonNodeToObject) {
        ArrayList<T> list = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            Iterator<JsonNode> elements = root.elements();
            while(elements.hasNext()) {
                JsonNode element = elements.next();
                T object = mapJsonNodeToObject.apply(element);
                list.add(object);
            }
        } catch (JsonProcessingException err) {
            throw new AppException("Expected json response");
        }

        return list;
    }

    private Product mapJsonToProduct(JsonNode jsonNode) {
        Double quantity = jsonNode.get("quantity").asDouble();
        JsonNode productNode = jsonNode.get("product");

        Long id = productNode.get("id").asLong();
        String name = productNode.get("name").asText();
        BigDecimal price = productNode.get("price").decimalValue();
        String image = productNode.get("image").asText();
        String unit = productNode.get("unit").asText();
        Integer discount = productNode.get("discount").get("percentage").asInt();
        String barcode = productNode.get("barcode").asText();
        String description = productNode.get("description").asText();

        return new Product(id, name, quantity, price, discount, unit, image, barcode, description);
    }

    private User mapJsonToUser(JsonNode jsonNode) {
        try {
            User user = new ObjectMapper().readValue(jsonNode.toString(), User.class);
            JsonNode profile = jsonNode.get("profile");

            String name = profile.get("name").asText();
            String surname = profile.get("surname").asText();
            String address = profile.get("address").asText();
            String city = profile.get("city").asText();
            String country = profile.get("country").asText();
            String phoneNumber = profile.get("phoneNumber").asText();

            user.setPassword(jsonNode.get("password").asText());
            user.setName(name);
            user.setSurname(surname);
            user.setAddress(address);
            user.setCity(city);
            user.setCountry(country);
            user.setPhoneNumber(phoneNumber);

            return user;
        } catch (JsonProcessingException e) {
            throw new AppException("Expected valid json response");
        }
    }

    private CashRegister mapJsonToCashRegister(JsonNode jsonNode) {
        String name = jsonNode.get("name").asText();
        Long id = jsonNode.get("id").asLong();
        String uuid = jsonNode.get("uuid").asText();
        return new CashRegister(id, name, uuid, false, false);
    }

    private List<Table> fetchTablesFromMain() {

        String uri = String.format("/offices/%d/tables", officeID);
        String json = httpClientService.makeGetRequest(uri);
        return jsonListToObjectList(json, this::mapJsonToTable);
    }

    private Table mapJsonToTable(JsonNode jsonNode) {
        Long id = jsonNode.get("id").asLong();
        int tableNUmber = jsonNode.get("tableNumber").asInt();
        return new Table(id, tableNUmber);
    }

}
