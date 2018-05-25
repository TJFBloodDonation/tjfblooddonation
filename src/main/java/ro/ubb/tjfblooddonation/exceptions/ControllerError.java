package ro.ubb.tjfblooddonation.exceptions;

public class ControllerError extends BaseException{
    public ControllerError() {
    }

    public ControllerError(String message) {
        super(message);
    }

    public ControllerError(String message, Throwable cause) {
        super(message, cause);
    }
}
