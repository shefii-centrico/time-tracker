-- V4: Add G Prasath as TEAM_LEAD
INSERT IGNORE INTO users (username, password, role, full_name, email, department_id)
VALUES (
    'GBS05321',
    '$2a$10$jM3SzG97w5nIdt/XDj6W4e2sohrK0nPsPgKjHsdHIerN71qtpnRVy',
    'TEAM_LEAD',
    'G Prasath',
    'prasath.g@centrico.tech',
    (SELECT id FROM departments WHERE name = 'Core Banking' LIMIT 1)
);
