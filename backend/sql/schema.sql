CREATE TABLE calls (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    date           DATE        NOT NULL,
    time           TIME        NOT NULL,
    type           ENUM('Incoming','Outgoing','Missed') NOT NULL,
    device_number  VARCHAR(20) NOT NULL,
    client_number  VARCHAR(20) NOT NULL,
    ring_duration  INT         NOT NULL,
    call_duration  INT         NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
