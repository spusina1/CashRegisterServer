package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.models.transactions.ReceiptStatus;
import ba.unsa.etf.si.local_server.responses.WebSocketNotification;
import ba.unsa.etf.si.local_server.responses.WebSocketPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;

@RequiredArgsConstructor
public class MainStompSessionHandler implements StompSessionHandler {
    private final ReceiptService receiptService;
    private final MainSyncUpService mainSyncUpService;

    @Override
    public void afterConnected(StompSession session, StompHeaders stompHeaders) {
        System.out.println("Connected; Session id: " + session.getSessionId());
        session.subscribe("/topic/cash_server", this);
    }

    @Override
    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
        System.err.println("Handle exception!");
        throwable.printStackTrace();
    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable throwable) {
        System.err.println("Handle transport error!");
        throwable.printStackTrace();
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
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void handleOfficeProductsAdd(WebSocketPayload payload) {
        try {
            JsonNode jsonNode = getJsonFromPayload(payload);
            JsonNode inventory = jsonNode.get("inventory");
            mainSyncUpService.syncProducts();
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
