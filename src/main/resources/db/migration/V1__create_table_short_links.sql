CREATE TABLE short_links (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    short_code      VARCHAR(16)     NOT NULL UNIQUE,
    original_url    VARCHAR(2048)   NOT NULL,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at      TIMESTAMP       NOT NULL
);
