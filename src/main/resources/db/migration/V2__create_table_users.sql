CREATE TABLE users (
    id              UUID                PRIMARY KEY,
    full_name       VARCHAR(100)        NOT NULL,
    email           VARCHAR(65)         NOT NULL UNIQUE,
    password        VARCHAR(255)        NOT NULL,
    role            VARCHAR(20)         NOT NULL
);