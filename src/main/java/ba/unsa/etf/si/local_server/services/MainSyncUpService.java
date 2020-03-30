package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.AppException;
import ba.unsa.etf.si.local_server.exceptions.ResourceNotFoundException;
import ba.unsa.etf.si.local_server.models.Product;
import ba.unsa.etf.si.local_server.models.User;
import ba.unsa.etf.si.local_server.requests.MainLoginRequest;
import ba.unsa.etf.si.local_server.responses.MainLoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class MainSyncUpService {
    private final RestTemplate restTemplate;
    private final UserService userService;
    private final ProductService productService;

    @Value("${main_server.base_URI}")
    private String baseURI;

    @Value("${main_server.login_username}")
    private String username;

    @Value("${main_server.login_password}")
    private String password;

    @Value("${main_server.office_id}")
    private int officeID;

    @Scheduled(cron = "${cron.main_fetch}")
    public void syncDatabases() {
        System.out.println("Synchronizing databases...");
        List<User> users = fetchUsersFromMain();
        List<Product> products = fetchProductsFromMain();

        userService.batchInsertUsers(users);
        productService.batchInsertProducts(new ArrayList<>());
    }

    private List<Product> fetchProductsFromMain() {
        String uri = String.format("%s/offices/%d/products", baseURI, officeID);
        String json = makeGetRequest(uri);
        return jsonListToObjectList(json, this::mapJsonToProduct);
    }

    private List<User> fetchUsersFromMain() {
        String uri = String.format("%s/office-employees", baseURI);
        String json = makeGetRequest(uri);
        return jsonListToObjectList(json, this::mapJsonToUser);
    }

    private String makeGetRequest(String uri) {
        MainLoginRequest mainLoginRequest = new MainLoginRequest(username, password);
        String token = obtainOfficeManagerToken(mainLoginRequest);

        HttpHeaders headers = new HttpHeaders() {{
            String bearerToken = String.format("Bearer %s", token);
            set(AUTHORIZATION, bearerToken);
        }};

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        return response.getBody();
    }

    private String obtainOfficeManagerToken(MainLoginRequest request) {
        try {
            String uri = baseURI + "/auth/login";
            return restTemplate.postForObject(uri, request, MainLoginResponse.class).getToken();
        } catch (HttpClientErrorException err) {
            err.printStackTrace();
            throw new ResourceNotFoundException("Could not get login response from the main server");
        }
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

        return new Product(id, name, quantity, price, discount, unit, image, null);
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

            System.out.println(user.toString());

            return user;
        } catch (JsonProcessingException e) {
            throw new AppException("Expected valid json response");
        }
    }

}
