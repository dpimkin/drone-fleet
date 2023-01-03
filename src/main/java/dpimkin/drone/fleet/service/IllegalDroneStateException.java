package dpimkin.drone.fleet.service;

public class IllegalDroneStateException extends IllegalStateException {
    public IllegalDroneStateException() {
    }

    public IllegalDroneStateException(String s) {
        super(s);
    }

    public IllegalDroneStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalDroneStateException(Throwable cause) {
        super(cause);
    }
}
