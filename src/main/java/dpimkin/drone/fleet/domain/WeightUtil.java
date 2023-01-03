package dpimkin.drone.fleet.domain;

import java.util.List;

public abstract class WeightUtil {

    public static int calculateWeight(List<MedicationPayload> payloadList) {
        return payloadList.stream()
                .mapToInt(medication -> medication.weight)
                .sum();
    }

    private WeightUtil() {
    }

}
