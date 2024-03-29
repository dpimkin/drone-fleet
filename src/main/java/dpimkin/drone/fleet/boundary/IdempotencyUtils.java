package dpimkin.drone.fleet.boundary;

public abstract class IdempotencyUtils {
    public static boolean isInvalidIdempotencyKey(String key) {
        if (null == key) {
            return true;
        }
        return key.isEmpty();
    }

    private IdempotencyUtils() {
    }
}
