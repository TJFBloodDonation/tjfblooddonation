package ro.ubb.tjfblooddonation.exceptions;

public class ServiceError extends RuntimeException {
    public ServiceError(String s) {
        super(s);
    }
}
