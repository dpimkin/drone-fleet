package dpimkin.drone.fleet.service;

public class LowBatteryException extends RuntimeException {
    public LowBatteryException() {
    }

    public LowBatteryException(String message) {
        super(message);
    }

    public LowBatteryException(String message, Throwable cause) {
        super(message, cause);
    }

    public LowBatteryException(Throwable cause) {
        super(cause);
    }

    public LowBatteryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
