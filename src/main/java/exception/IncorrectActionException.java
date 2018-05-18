package exception;

public class IncorrectActionException extends Exception {
    public IncorrectActionException(String protocol) {
        System.err.println("\n" + protocol + " protocol is incorrect.\n");
        System.exit(1);
    }

    public IncorrectActionException() {
        System.err.println("\nProtocol is incorrect.\n");
        System.exit(1);
    }
}
