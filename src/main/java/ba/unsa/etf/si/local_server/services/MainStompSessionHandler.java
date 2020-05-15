package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus;
import ba.unsa.etf.si.local_server.responses.WebSocketNotification;
import ba.unsa.etf.si.local_server.responses.WebSocketPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;

@RequiredArgsConstructor
public class MainStompSessionHandler implements StompSessionHandler {
    private final ReceiptService receiptService;
    private final MainSyncUpService mainSyncUpService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final Logger logger = LoggerFactory.getLogger(MainStompSessionHandler.class);

    @Override
    public void afterConnected(StompSession session, StompHeaders stompHeaders) {
        System.out.println("Connected; Session id: " + session.getSessionId());
        session.subscribe("/topic/cash_server", this);
    }

    @Override
    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
        logger.error(String.format("An exception occurred (websocket): %s", throwable.getMessage()));
    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable throwable) {
        logger.error(String.format("An error occurred (websocket): %s", throwable.getMessage()));
    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        return WebSocketNotification.class;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object object) {
        WebSocketNotification notification = (WebSocketNotification) object;
        WebSocketPayload payload = notification.getPayload();

        switch (payload.getAction()) {
            case "receipt_status_update":
                handleReceiptStatusUpdate(payload);
                break;
            case "office_products_add":
                handleOfficeProductsAdd(payload);
                break;
            default:
                System.out.println("Unhandled action from WebSocketPayload");
        }

    }

    private void handleReceiptStatusUpdate(WebSocketPayload payload) {
        try {
            JsonNode jsonNode = getJsonFromPayload(payload);
            String receiptId = jsonNode.get("receiptId").asText();
            String statusString = jsonNode.get("status").asText();
            ReceiptStatus status = Enum.valueOf(ReceiptStatus.class, statusString);
            receiptService.updateReceiptStatus(receiptId, status);
            simpMessagingTemplate.convertAndSend("/topic/receipt_status_update", jsonNode.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void handleOfficeProductsAdd(WebSocketPayload payload) {
        try {
            JsonNode jsonNode = getJsonFromPayload(payload);
            JsonNode inventory = jsonNode.get("inventory");
            mainSyncUpService.syncProducts();
            simpMessagingTemplate.convertAndSend("/topic/inventory_update", inventory.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private JsonNode getJsonFromPayload(WebSocketPayload payload) throws JsonProcessingException {
        String jsonString = payload.getDescription();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(jsonString);
    }

}
