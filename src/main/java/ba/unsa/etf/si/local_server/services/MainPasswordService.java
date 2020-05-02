package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.AppException;
import ba.unsa.etf.si.local_server.models.transactions.Receipt;
import ba.unsa.etf.si.local_server.requests.NewPasswordRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class MainPasswordService {
    private final HttpClientService httpClientService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MainPasswordService(@Lazy HttpClientService httpClientService) {
        this.httpClientService = httpClientService;
    }
    public void postToMain(NewPasswordRequest newPasswordRequest) {
        try {
            String json = objectMapper.writeValueAsString(newPasswordRequest);
            String response = httpClientService.makePutRequest("/auth/office-changePassword", json);

            JsonNode jsonNode = objectMapper.readTree(response);
            checkResponseStatus(jsonNode);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (HttpClientErrorException err) {
            throw new AppException(err.getMessage());
        }
    }
    private void checkResponseStatus(JsonNode jsonNode) {
        JsonNode statusCodeProperty = jsonNode.get("status");

        if(statusCodeProperty != null) {
            int statusCode = statusCodeProperty.asInt(200);
            if (statusCode >= 400) {
                throw new AppException("Response with status code " + statusCode + " while posting new password to main");
            }
        }
    }

}
