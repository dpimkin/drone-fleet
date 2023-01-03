package dpimkin.drone.fleet.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Settings {

    @Value("${drone-flee.low-battery.threshold:25}")
    int lowBatteryThreshold;


}
