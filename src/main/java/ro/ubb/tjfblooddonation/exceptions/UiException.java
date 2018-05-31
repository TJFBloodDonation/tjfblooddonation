package ro.ubb.tjfblooddonation.exceptions;

public class UiException extends BaseException{
    public UiException() {
    }

    public UiException(String message) {
        super(message);
    }

    public UiException(String message, Throwable cause) {
        super(message, cause);
    }
}
