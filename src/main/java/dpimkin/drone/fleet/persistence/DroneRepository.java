package dpimkin.drone.fleet.persistence;


import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DroneRepository extends ReactiveCrudRepository<DroneEntity, Long> {

    Mono<DroneEntity> findBySerialNumber(String serialNumber);


    @Query("""
            SELECT * FROM drone
            WHERE battery_cap < $1
            """)
    Flux<DroneEntity> findDronesWithLowBattery(int batteryCapThreshold);

    @Query("""
            SELECT * FROM drone
            WHERE (drone_state = 'IDLE' OR drone_state = 'LOADING') AND battery_cap >= $1 
            LIMIT $2             
            """)
    Flux<DroneEntity> findAvailableDrones(int batteryCapThreshold, int limit);

    @Query("""
        SELECT * FROM drone
        WHERE sn = $1
        FOR UPDATE SKIP LOCKED;
        """)
    Mono<DroneEntity> lockDroneBySerialNumberForLoading(String sn);


}
