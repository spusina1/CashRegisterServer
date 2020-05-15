package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.AppException;
import ba.unsa.etf.si.local_server.models.transactions.Receipt;
import ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
public class MainReceiptService {
    private final HttpClientService httpClientService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public void postReceiptToMain(Receipt receipt) {
        try {
            String json = objectMapper.writeValueAsString(receipt);
            String response = httpClientService.makePostRequest("/receipts", json);

            JsonNode jsonNode = objectMapper.readTree(response);
            checkResponseStatus(jsonNode);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (HttpClientErrorException err) {
            throw new AppException("Failed to post receipt to main!");
        }
    }

    private void checkResponseStatus(JsonNode jsonNode) {
        JsonNode statusCodeProperty = jsonNode.get("status");

        if(statusCodeProperty != null) {
            int statusCode = statusCodeProperty.asInt(200);
            if (statusCode >= 400) {
                throw new AppException("Response with status code " + statusCode + " while posting receipt to main");
            }
        }
    }

}
