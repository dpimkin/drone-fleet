package dpimkin.drone.fleet.api;

public abstract class Endpoints {
    private static final String API_VERSION_PREFIX = "/api/v1/";

    public static final String DISPATCH_ENDPOINT = API_VERSION_PREFIX + "dispatch";
    public static final String DRONE_CRUD_ENDPOINT = API_VERSION_PREFIX + "drone";

    public static final String AUDIT_ENDPOINT = API_VERSION_PREFIX + "audit";

    private Endpoints() {
    }
}
