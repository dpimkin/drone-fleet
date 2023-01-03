package dpimkin.drone.fleet.service;

public class NotEnoughWeightCapacityException extends RuntimeException {

    public NotEnoughWeightCapacityException() {
    }

    public NotEnoughWeightCapacityException(String message) {
        super(message);
    }

    public NotEnoughWeightCapacityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughWeightCapacityException(Throwable cause) {
        super(cause);
    }

    public NotEnoughWeightCapacityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
