package ro.ubb.tjfblooddonation.exceptions;

public class ServiceError extends BaseException {

    public ServiceError() {
    }

    public ServiceError(String message) {
        super(message);
    }

    public ServiceError(String message, Throwable cause) {
        super(message, cause);
    }
}
