CREATE TABLE IF NOT EXISTS http_logs (
    id SERIAL PRIMARY KEY,
    method VARCHAR(10),
    uri VARCHAR(255),
    status INT,
    duration_ms INT,
    timestamp TIMESTAMP,
    user_id INT,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX idx_http_log_timestamp ON http_logs(timestamp);