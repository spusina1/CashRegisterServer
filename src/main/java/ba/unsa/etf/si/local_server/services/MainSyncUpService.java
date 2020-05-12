package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.AppException;
import ba.unsa.etf.si.local_server.models.Business;
import ba.unsa.etf.si.local_server.models.CashRegister;
import ba.unsa.etf.si.local_server.models.Product;
import ba.unsa.etf.si.local_server.models.Table;
import ba.unsa.etf.si.local_server.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

@Service
public class MainSyncUpService {
    private final HttpClientService httpClientService;
    private final UserService userService;
    private final ProductService productService;
    private final BusinessService businessService;
    private final TableService tableService;

    public MainSyncUpService(
            HttpClientService httpClientService,
            UserService userService,
            ProductService productService,
            BusinessService businessService,
            TableService tableService
    ) {
        this.httpClientService = httpClientService;
        this.userService = userService;
        this.productService = productService;
        this.businessService = businessService;
        this.tableService = tableService;
    }

    private boolean restaurant;

    @Scheduled(cron = "${cron.main_fetch}")
    public void syncDatabases() {
        System.out.println("Synchronizing databases...");

        Business business = fetchBusinessFromMain();
        List<User> users = fetchUsersFromMain();
        List<Product> products = fetchProductsFromMain();
        List<Table> tables = fetchTablesFromMain();
        
        userService.batchInsertUsers(users);
        productService.batchInsertProducts(products);
        businessService.updateBusinessInfo(business);
        tableService.batchInsertTables(tables);

        System.out.println("Yaaay, Synchronisation complete!");
    }

    private List<Product> fetchProductsFromMain() {
        Long officeID = businessService.getCurrentBusiness().getOfficeId();
        String uri = String.format("/offices/%d/products", officeID);
        String json = httpClientService.makeGetRequest(uri);
        return jsonListToObjectList(json, this::mapJsonToProduct);
    }

    private List<User> fetchUsersFromMain() {
        String uri = "/office-employees";
        String json = httpClientService.makeGetRequest(uri);
        return jsonListToObjectList(json, this::mapJsonToUser);
    }

    // TODO: Fetch data based on user account and not on ids
    private Business fetchBusinessFromMain() {
//        String uri = String.format("/business/%d/office-details/%d", businessID, officeID);
        String uri = String.format("/business/%d/office-details/%d", 1, 1);
        String json = httpClientService.makeGetRequest(uri);
        ObjectMapper objectMapper = new ObjectMapper();

        Business business = new Business();
        String jsonArray;

        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            jsonArray = jsonNode.get("cashRegisters").toString();

            // TODO: Check json property names
            Long businessId = jsonNode.get("id").asLong();
            Long officeId = jsonNode.get("officeId").asLong();
            String businessName = jsonNode.get("businessName").asText();
            restaurant = jsonNode.get("restaurant").asBoolean();
            String language = jsonNode.get("language").asText();
            String startTime = jsonNode.get("startTime").asText();
            String endTime = jsonNode.get("endTime").asText();
            String syncTime = jsonNode.get("syncTime").asText();
            String placeName = jsonNode.get("placeName").asText();

            business.setBusinessId(businessId);
            business.setOfficeId(officeId);
            business.setBusinessName(businessName);
            business.setRestaurant(restaurant);
            business.setLanguage(language);
            business.setStartTime(startTime);
            business.setEndTime(endTime);
            business.setSyncTime(syncTime);
            business.setPlaceName(placeName);

        } catch (JsonProcessingException e) {
            throw new AppException("Expected json response");
        }
        List<CashRegister> cashRegisters = jsonListToObjectList(jsonArray, this::mapJsonToCashRegister);
        business.setCashRegisters(cashRegisters);

        return business;
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
        Double pdv = productNode.get("pdv").asDouble();

        return new Product(id, name, quantity, price, discount, unit, image, barcode, description, pdv);
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
            String email = profile.get("email").asText();

            user.setPassword(jsonNode.get("password").asText());
            user.setName(name);
            user.setSurname(surname);
            user.setAddress(address);
            user.setCity(city);
            user.setCountry(country);
            user.setPhoneNumber(phoneNumber);
            user.setEmail(email);

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
        if(!restaurant) return jsonListToObjectList("[]", this::mapJsonToTable);
        Long officeId = businessService.getCurrentBusiness().getOfficeId();
        String uri = String.format("/offices/%d/tables", officeId);
        String json = httpClientService.makeGetRequest(uri);
        return jsonListToObjectList(json, this::mapJsonToTable);
    }

    private Table mapJsonToTable(JsonNode jsonNode) {
        Long id = jsonNode.get("id").asLong();
        String tableNUmber = jsonNode.get("tableName").asText();
        return new Table(id, tableNUmber);
    }

}
