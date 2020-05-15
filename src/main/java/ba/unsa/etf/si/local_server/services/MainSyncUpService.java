package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.AppException;
import ba.unsa.etf.si.local_server.models.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class MainSyncUpService {
    private final HttpClientService httpClientService;
    private final UserService userService;
    private final ProductService productService;
    private final BusinessService businessService;
    private final TableService tableService;
    private final CashRegisterService cashRegisterService;
    private final TaskScheduleService taskScheduleService;

    private final Logger logger = LoggerFactory.getLogger(MainSyncUpService.class);

    public void syncDatabases() {
        syncBusiness();
        syncProducts();
        syncUsers();
        syncTables();
        scheduleTasks();
        openCloseCashRegister();
    }

    public void syncBusiness() {
        logger.info("Business sync started...");
        Business business = fetchBusinessFromMain();
        businessService.updateBusinessInfo(business);
        logger.info("Business sync finished!");
    }

    public void syncProducts() {
        logger.info("Products sync started...");
        List<Product> products = fetchProductsFromMain();
        productService.batchInsertProducts(products);
        logger.info("Products sync finished!");
    }

    public void syncUsers() {
        logger.info("Users sync started...");
        List<User> users = fetchUsersFromMain();
        userService.batchInsertUsers(users);
        logger.info("Users sync finished!");
    }

    public void syncTables() {
        logger.info("Tables sync started...");
        List<Table> tables = fetchTablesFromMain();
        tableService.batchInsertTables(tables);
        logger.info("Tables sync finished!");
    }

    private void scheduleTasks() {
        Business business = businessService.getCurrentBusiness();
        taskScheduleService.scheduleSync(business.getSyncTime(), this::syncDatabases);
        taskScheduleService.scheduleCashRegisterOpen(business.getStartTime(), cashRegisterService::openRegisters);
        taskScheduleService.scheduleCashRegisterClose(business.getEndTime(), cashRegisterService::closeRegisters);
        logger.info(String.format("Synchronisation scheduled for %s", business.getSyncTime()));
        logger.info(String.format("Cash registers open scheduled for %s", business.getStartTime()));
        logger.info(String.format("Cash registers close scheduled for %s", business.getEndTime()));
    }

    private void openCloseCashRegister() {
        Business business = businessService.getCurrentBusiness();
        String startTime = business.getStartTime();
        String endTime = business.getEndTime();
        LocalDateTime now = LocalDateTime.now();
        String currentTime = String.format("%02d:%02d", now.getHour(), now.getMinute());
        boolean cashRegisterOpen = currentTime.compareTo(startTime) >= 0 && currentTime.compareTo(endTime) < 0;
        cashRegisterService.setIsCashRegisterOpen(cashRegisterOpen);
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

    private Business fetchBusinessFromMain() {
        String uri = "/business/office-details";
        String json = httpClientService.makeGetRequest(uri);
        ObjectMapper objectMapper = new ObjectMapper();

        Business business = new Business();
        String jsonArray;

        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            jsonArray = jsonNode.get("cashRegisters").toString();

            Long businessId = jsonNode.get("businessId").asLong();
            Long officeId = jsonNode.get("officeId").asLong();
            String businessName = jsonNode.get("businessName").asText();
            boolean restaurant = jsonNode.get("restaurant").asBoolean();
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

        Product product = new Product(
                id, name, quantity, price, discount, unit, image,
                barcode, description, pdv, null, null
        );

        ItemType itemType;
        List<ProductItem> productItems;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            itemType = objectMapper.readValue(productNode.get("itemType").toString(), ItemType.class);
            String productItemsJson = productNode.get("items").toString();
            productItems = jsonListToObjectList(productItemsJson, this::mapJsonToProductItem);
            productItems.forEach(productItem -> productItem.setProduct(product));
        } catch (JsonProcessingException e) {
            throw new AppException("Invalid product json from main");
        }

        product.setItemType(itemType);
        product.setProductItems(productItems);

        return product;
    }

    private ProductItem mapJsonToProductItem(JsonNode jsonNode) {
        Double value = jsonNode.get("value").asDouble();
        JsonNode itemNode = jsonNode.get("item");

        Long id = itemNode.get("id").asLong();
        String name = itemNode.get("name").asText();
        String unit = itemNode.get("unit").asText();
        Item item = new Item(id,name, unit);
        return new ProductItem(null, null, item, value);
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
        Business currentBusiness = businessService.getCurrentBusiness();
        if(!currentBusiness.isRestaurant()) {
            return jsonListToObjectList("[]", this::mapJsonToTable);
        }
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
