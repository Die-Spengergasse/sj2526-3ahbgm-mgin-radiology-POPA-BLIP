USE datenbankRadiology;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS patient;
DROP TABLE IF EXISTS device;

CREATE TABLE device (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    type VARCHAR(255),
    location VARCHAR(255)
);

CREATE TABLE patient (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    social_security_number VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    gender VARCHAR(255),
    birth_date DATE
);

CREATE TABLE reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT,
    device_id BIGINT,
    date DATE,
    time_from VARCHAR(255),
    time_to VARCHAR(255),
    body_region VARCHAR(255),
    comment VARCHAR(255),
    FOREIGN KEY (patient_id) REFERENCES patient(id),
    FOREIGN KEY (device_id) REFERENCES device(id)
);

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO device (name, type, location) VALUES ('MRT-1', 'MR', 'Raum 101');
INSERT INTO device (name, type, location) VALUES ('CT-1', 'CT', 'Raum 102');
INSERT INTO device (name, type, location) VALUES ('RX-1', 'Röntgen', 'Raum 103');

INSERT INTO patient (social_security_number, first_name, last_name, gender, birth_date) VALUES ('1234567890', 'Max', 'Mustermann', 'Männlich', '1990-05-15');
INSERT INTO patient (social_security_number, first_name, last_name, gender, birth_date) VALUES ('0987654321', 'Maria', 'Musterfrau', 'Weiblich', '1985-03-22');
INSERT INTO patient (social_security_number, first_name, last_name, gender, birth_date) VALUES ('1122334455AB', 'Klaus', 'Beispiel', 'Männlich', '2000-11-01');

INSERT INTO reservation (patient_id, device_id, date, time_from, time_to, body_region, comment) VALUES (1, 1, '2026-05-25', '08:00', '08:30', 'Kopf', 'Erstuntersuchung');
INSERT INTO reservation (patient_id, device_id, date, time_from, time_to, body_region, comment) VALUES (2, 2, '2026-05-25', '09:00', '09:30', 'Brust', 'Kontrolluntersuchung');
INSERT INTO reservation (patient_id, device_id, date, time_from, time_to, body_region, comment) VALUES (3, 3, '2026-05-26', '10:00', '10:15', 'Extremität', 'Nachsorge');