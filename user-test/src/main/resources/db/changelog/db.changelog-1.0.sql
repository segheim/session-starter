--liquibase formatted sql

--changeset Savitsky_E:1
CREATE TABLE IF NOT EXISTS "users"
(
       id           BIGDERIAL CONSTRAINT sessions_pk PRIMARY KEY,
       "login"      VARCHAR NOT NULL,
       "password"   VARCHAR NOT NULL
);