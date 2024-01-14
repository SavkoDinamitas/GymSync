package raf.sk.notificationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BanException extends ResponseStatusException {
    public BanException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
