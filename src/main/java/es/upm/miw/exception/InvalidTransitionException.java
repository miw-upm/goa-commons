package es.upm.miw.exception;

public class InvalidTransitionException extends BusinessRuleException {
    public InvalidTransitionException(String userMessage) {
        super(userMessage);
    }
}
