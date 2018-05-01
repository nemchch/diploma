package exceptions;

public class IncorrectTimeoutException extends Exception {
    public IncorrectTimeoutException(String message) {
        System.err.println(message);
    }

}
