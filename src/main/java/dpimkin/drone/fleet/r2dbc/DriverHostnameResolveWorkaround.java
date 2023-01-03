package dpimkin.drone.fleet.r2dbc;

import java.net.InetAddress;

public class DriverHostnameResolveWorkaround {

    public static void resolveDatabaseHostName() {
        try {
            System.setProperty("DB_HOST", InetAddress.getByName("app_db").getHostAddress());
        } catch (Exception e) {
        }
    }
}
