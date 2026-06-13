package es.upm.miw.exception;

public class ClientBusinessException extends RuntimeException {
    public ClientBusinessException(String userMessage) {
        super(userMessage);
    }
}



