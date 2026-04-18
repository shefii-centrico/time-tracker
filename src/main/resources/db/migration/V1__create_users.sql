-- V1: Create users table for v1.0 multi-user support
-- Schema for tasks and time_logs is managed by Hibernate (ddl-auto=update)
-- This migration only creates the users table which is brand new in v1.0

CREATE TABLE IF NOT EXISTS users (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(20)  NOT NULL,
    full_name  VARCHAR(100)
);
