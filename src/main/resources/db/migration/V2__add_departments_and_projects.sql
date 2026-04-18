-- V2: Add departments, projects, project_assignments tables
-- and extend users table

CREATE TABLE IF NOT EXISTS departments (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS projects (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    client_name VARCHAR(255),
    start_date  DATE,
    end_date    DATE,
    status      VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_by  BIGINT,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS project_assignments (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id      BIGINT NOT NULL,
    user_id         BIGINT NOT NULL,
    role_in_project VARCHAR(100),
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uq_project_user (project_id, user_id)
);

-- Extend users table
ALTER TABLE users ADD COLUMN email VARCHAR(255);
ALTER TABLE users ADD COLUMN department_id BIGINT;
ALTER TABLE users ADD CONSTRAINT fk_users_dept FOREIGN KEY (department_id) REFERENCES departments(id);

-- Seed default departments
INSERT IGNORE INTO departments (name, description) VALUES
    ('Core Banking',       'Core banking system development'),
    ('QA & Testing',       'Quality assurance and testing'),
    ('DevOps',             'Infrastructure and deployment'),
    ('Business Analysis',  'Requirements and business analysis'),
    ('Mobile Banking',     'Mobile application development');
