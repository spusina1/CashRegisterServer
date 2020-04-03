package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.exceptions.AppException;
import ba.unsa.etf.si.local_server.models.transactions.Receipt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class MainReceiptService {
    private final ReceiptService receiptService;
    private final HttpClientService httpClientService;

    public MainReceiptService(@Lazy ReceiptService receiptService, HttpClientService httpClientService) {
        this.receiptService = receiptService;
        this.httpClientService = httpClientService;
    }

    @Async
    public void pollReceiptStatus() {

    }

    public void postReceiptToMain(Receipt receipt) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String json = objectMapper.writeValueAsString(receipt);
            String response = httpClientService.makePostRequest("/receipts", json);

            JsonNode jsonNode = objectMapper.readTree(response);
            int statusCode = jsonNode.get("status").asInt(200);

            if (statusCode >= 400) {
                throw new AppException("Response with status code " + statusCode + " while posting receipt to main");
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (HttpClientErrorException err) {
            throw new AppException("Failed to post receipt to main!");
        }
    }

}
