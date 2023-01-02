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

CREATE SEQUENCE idempotent_operation_seq AS BIGINT;

CREATE TABLE operation_log
(
    id              BIGINT PRIMARY KEY DEFAULT nextval('idempotent_operation_seq'),
    idempotency_key VARCHAR(64),
    status          INTEGER,
    created         TIMESTAMP
);

CREATE UNIQUE INDEX idempotency_key_unique ON operation_log (idempotency_key);



-- TODO
-- INSERT INTO token(payload) VALUES ('FOO');
-- INSERT INTO token(payload) VALUES ('BAR');
--
-- CREATE SEQUENCE calendar_day_seq AS INTEGER;
--
-- CREATE TABLE calendar_day(
--                              id          INTEGER PRIMARY KEY DEFAULT nextval('calendar_day_seq'),
--                              country     SMALLINT NOT NULL,
--                              dmy         DATE NOT NULL,
--                              kind        SMALLINT
-- );