BEGIN;

CREATE SEQUENCE drone_seq AS BIGINT;
CREATE TABLE drone
(
    id          BIGINT PRIMARY KEY DEFAULT nextval('drone_seq'),
    sn          varchar(100) NOT NULL,
    battery_cap INTEGER      NOT NULL,
    weight_cap  INTEGER      NOT NULL,
    weight_max  INTEGER      NOT NULL,
    drone_type  varchar(30)  NOT NULL,
    drone_state varchar(30)  NOT NULL
);
CREATE UNIQUE INDEX sn_unique ON drone (sn);
CREATE INDEX drone_sn_battery_cap ON drone(sn,battery_cap);
CREATE INDEX drone_type_battery_cap ON drone(drone_type,battery_cap);

CREATE SEQUENCE medication_seq AS BIGINT;
CREATE TABLE medication_payload
(
    id       BIGINT PRIMARY KEY DEFAULT nextval('medication_seq'),
    drone_id BIGINT REFERENCES drone NOT NULL,
    code     varchar(64)             NOT NULL,
    "name"   varchar(150)            NOT NULL,
    weight   INTEGER                 NOT NULL,
    qty      INTEGER                 NOT NULL,
    image    VARCHAR(4096)
);
CREATE INDEX medication_drone_ref ON medication_payload (drone_id);


CREATE SEQUENCE operation_log_seq AS BIGINT;
CREATE TABLE operation_log
(
    id              BIGINT PRIMARY KEY DEFAULT nextval('operation_log_seq'),
    idempotency_key VARCHAR(64),
    status          INTEGER,
    created         TIMESTAMP
);
CREATE UNIQUE INDEX idempotency_key_unique ON operation_log (idempotency_key);

COMMIT;