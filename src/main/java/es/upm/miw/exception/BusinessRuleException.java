package es.upm.miw.exception;

public abstract class BusinessRuleException extends RuntimeException {
    protected BusinessRuleException(String userMessage) {
        super(userMessage);
    }
}



