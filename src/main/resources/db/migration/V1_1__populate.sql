BEGIN;
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('000',   0, 100, 100, 'LIGHTWEIGHT', 'IDLE');
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('001',  10, 100, 100, 'LIGHTWEIGHT', 'LOADING');
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('002',  20, 100, 100, 'LIGHTWEIGHT', 'IDLE');
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('003',  30, 100, 100, 'LIGHTWEIGHT', 'LOADED');
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('004',  40, 100, 100, 'LIGHTWEIGHT', 'IDLE');
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('005',  50, 100, 100, 'LIGHTWEIGHT', 'IDLE');
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('006',  60, 100, 100, 'LIGHTWEIGHT', 'IDLE');
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('007',  70, 100, 100, 'LIGHTWEIGHT', 'DELIVERING');
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('008',  80, 100, 100, 'LIGHTWEIGHT', 'DELIVERED');
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('009',  90, 100, 100, 'LIGHTWEIGHT', 'RETURNING');

COMMIT;