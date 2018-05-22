package ro.ubb.tjfblooddonation.exceptions;

public class LogInException extends BaseException {
    public LogInException() {
    }

    public LogInException(String message) {
        super(message);
    }

    public LogInException(String message, Throwable cause) {
        super(message, cause);
    }
}
