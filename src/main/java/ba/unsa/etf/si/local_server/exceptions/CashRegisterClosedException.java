package ba.unsa.etf.si.local_server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LOCKED)
public class CashRegisterClosedException extends RuntimeException {

    public CashRegisterClosedException(String message) {
        super(message);
    }

    public CashRegisterClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}