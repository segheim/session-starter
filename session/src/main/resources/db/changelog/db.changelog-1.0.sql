--liquibase formatted sql

--changeset Savitsky_E:1
CREATE TABLE IF NOT EXISTS "sessions"
(
       id           BIGSERIAL CONSTRAINT sessions_pk PRIMARY KEY,
       "login"      VARCHAR(50) NOT NULL CONSTRAINT sessions_pk2 UNIQUE,
       create_at    TIMESTAMP DEFAULT now()
);