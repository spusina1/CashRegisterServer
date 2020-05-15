package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.requests.WebSocketNotification;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;

public class MainStompSessionHandler implements StompSessionHandler {
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
    public void handleFrame(StompHeaders stompHeaders, Object payload) {
        WebSocketNotification notification = (WebSocketNotification) payload;
        System.out.println(String.format("Received: %s", notification.getPayload().getDescription()));

        // TODO: For receipt frame handler check the action and set product state
    }
}
