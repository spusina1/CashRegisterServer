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
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@AllArgsConstructor
@Service
public class MainSyncUpService {

    private final RestTemplate restTemplate;
    private final String baseURI = "https://main-server-si.herokuapp.com/api";
    private final MainLoginRequest mainLoginRequest = new MainLoginRequest("office1", "password");

    public String obtainOfficeManagerToken(MainLoginRequest request) {
        try {
            String uri = baseURI + "/auth/login";
            return restTemplate.postForObject(uri, request, MainLoginResponse.class).getToken();
        } catch (HttpClientErrorException err) {
            throw new ResourceNotFoundException("Could not get login response from the main server");
        }
    }

    public List<Product> syncProductsFromMain() {
        String uri = baseURI + "/offices/1/products";
        String token = obtainOfficeManagerToken(mainLoginRequest);

        HttpHeaders headers = new HttpHeaders() {{
            String bearerToken = String.format("Bearer %s", token);
            set(AUTHORIZATION, bearerToken);
        }};

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        ArrayList<Product> products = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            System.out.println();
            Iterator<JsonNode> elements = root.elements();
            while(elements.hasNext()) {
                JsonNode element = elements.next();

                Double quantity = element.get("quantity").asDouble();
                JsonNode productNode = element.get("product");

                Long id = productNode.get("id").asLong();
                String name = productNode.get("name").toString();
                BigDecimal price = productNode.get("price").decimalValue();
                String image = productNode.get("image").toString();
                String unit = productNode.get("unit").toString();
                Integer discount = productNode.get("discount").get("percentage").asInt();

                Product product = new Product(id, name, quantity, price, discount, unit, image, null);

                System.out.println(product.toString());
                products.add(product);
            }
        } catch (JsonProcessingException err) {
            throw new AppException("Expected json response");
        }

        return products;
    }

    private Product mapJsonToProduct(JsonNode jsonNode) {
        return null;
    }

    public List<User> syncUsersFromMain() {
        return null;
    }

}
