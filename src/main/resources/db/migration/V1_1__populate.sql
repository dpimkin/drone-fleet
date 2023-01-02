BEGIN;
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('000',   5,   2, 100, 'HEAVYWEIGHT', 'IDLE'); -- charging
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('001',  83,   0, 250, 'MIDDLEWEIGHT', 'LOADING');
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('002',  76, 100, 100, 'LIGHTWEIGHT', 'IDLE');
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('003',  60,   5, 325, 'CRUISERWEIGHT', 'LOADED');
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('004',  50, 100, 100, 'LIGHTWEIGHT', 'IDLE');
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('005',  40, 100, 100, 'LIGHTWEIGHT', 'LOADING');
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('006',  30, 100, 100, 'LIGHTWEIGHT', 'IDLE');
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('007',  24, 100, 450, 'HEAVYWEIGHT', 'DELIVERING');
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('008',  15, 100, 100, 'LIGHTWEIGHT', 'DELIVERED');
INSERT INTO drone(sn,battery_cap,weight_cap,weight_max,drone_type,drone_state) VALUES ('009',   3, 100, 100, 'LIGHTWEIGHT', 'RETURNING'); -- critical

INSERT INTO medication_payload(drone_id,code,"name",weight,qty,image) VALUES (2,'44441-2222-9', 'thiazide diuretics', 50, 1, 'https://upload.wikimedia.org/wikipedia/commons/thumb/e/e1/Captopril_skeletal.svg/420px-Captopril_skeletal.svg.png');
INSERT INTO medication_payload(drone_id,code,"name",weight,qty,image) VALUES (2,'55552-3333-8', 'angiotensin-converting enzyme (ACE) inhibitors', 50, 1, 'https://almostadoctor.co.uk/wp-content/uploads/2017/06/RAAS-768x557.webp');
INSERT INTO medication_payload(drone_id,code,"name",weight,qty,image) VALUES (2,'22223-2222-7', 'calcium channel blockers', 50, 1, 'https://upload.wikimedia.org/wikipedia/commons/thumb/5/5b/Dipines.svg/330px-Dipines.svg.png');
INSERT INTO medication_payload(drone_id,code,"name",weight,qty,image) VALUES (2,'44455-1122-6', 'beta-blockers', 50, 1, 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Propranolol.svg/420px-Propranolol.svg.png');
INSERT INTO medication_payload(drone_id,code,"name",weight,qty,image) VALUES (2,'66655-2252-5', 'Prazosin', 50, 1, 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/db/Prazosin.svg/300px-Prazosin.svg.png');

INSERT INTO medication_payload(drone_id,code,"name",weight,qty,image) VALUES (4,'11111-2222-3', 'Snakevenom antidote', 320, 32, 'http://bioweb.uwlax.edu/bio203/2011/spohnhol_john/CroFab%20antivenom.JPG');
INSERT INTO medication_payload(drone_id,code,"name",weight,qty,image) VALUES (8,'50090-4488-0', 'Insulin', 350, 10, 'https://images.ctfassets.net/yixw23k2v6vo/m8WzC4ahfjXdWcQgJYxkK/4555ba86c62e88c4a6114b708d80f603/iStock-1250471519.jpg');

COMMIT;