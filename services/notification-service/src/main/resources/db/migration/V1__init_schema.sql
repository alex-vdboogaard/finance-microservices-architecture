CREATE TABLE IF NOT EXISTS notification
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT NOT NULL,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255) DEFAULT NULL,
    timestamp   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_notification_user_id ON notification (user_id);
