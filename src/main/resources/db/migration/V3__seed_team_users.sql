-- V3: Seed team user accounts
-- Login: username = GBS code, password = GBS code (BCrypt hashed)
-- Passwords were generated with BCryptPasswordEncoder (strength 10)

INSERT IGNORE INTO users (username, password, role, full_name, email, department_id)
SELECT
    u.username, u.password, u.role, u.full_name, u.email,
    (SELECT id FROM departments WHERE name = 'Core Banking' LIMIT 1) AS department_id
FROM (
    SELECT 'GBS07157'                                               AS username,
           '$2a$10$.LvgpNWsMG6oFwB9YcNnre4YhVOxtxy7/sb2OqxuEXyeLqqIi84Km' AS password,
           'ADMIN'                                                  AS role,
           'Mohammed Shafeeq'                                       AS full_name,
           'mohammed.shafeeq@centrico.tech'                         AS email
    UNION ALL
    SELECT 'GBS05301',
           '$2a$10$5Uvgc0fgST7ps2FKNIwTR.ynY.dcM6X/G0wCsXODTwEwXb59ZVj2m',
           'EMPLOYEE', 'B Vinothkumar', 'vinothkumar.b@centrico.tech'
    UNION ALL
    SELECT 'GBS07163',
           '$2a$10$dHklHYxHG8g7jBmYN675R.HGJ2t1DKACx5C/EKXcOIavxo6/cXta6',
           'EMPLOYEE', 'G Iswariya', 'iswariya.g@centrico.tech'
    UNION ALL
    SELECT 'GBS07180',
           '$2a$10$gw0jT9zdMXxvbmID0g/4XO8svmzVSZTF.vtvTyllD0tuta3k7w.iy',
           'EMPLOYEE', 'M Kumaravel', 'kumaravel.m@centrico.tech'
    UNION ALL
    SELECT 'GBS04442',
           '$2a$10$GWKeAoLSqNEpIBiYlbNFnOejYh2SuK7iuOugXllePAsQhSJux0ZDS',
           'EMPLOYEE', 'Michaelraj T', 'michaelraj.t@centrico.tech'
    UNION ALL
    SELECT 'GBS04423',
           '$2a$10$UDiYsDE8KTqs9HSXdiCea.KQmDborJZJQmTDpZEC8LcOCCQbniKJG',
           'EMPLOYEE', 'Udayakumar P', 'udayakumar.p@centrico.tech'
    UNION ALL
    SELECT 'admin',
           '$2a$10$S6sBpPuOrWwTJ9jKYl1r4eJjjAls1sQTxnCSaVtyYGgTNqcOKG6Ya',
           'ADMIN', 'System Admin', NULL
) u;

-- Fix emails that were seeded with wrong values by DataInitializer
UPDATE users SET email = 'vinothkumar.b@centrico.tech'   WHERE username = 'GBS05301';
UPDATE users SET email = 'iswariya.g@centrico.tech'      WHERE username = 'GBS07163';
UPDATE users SET email = 'kumaravel.m@centrico.tech'     WHERE username = 'GBS07180';
UPDATE users SET email = 'michaelraj.t@centrico.tech'    WHERE username = 'GBS04442';
UPDATE users SET email = 'mohammed.shafeeq@centrico.tech' WHERE username = 'GBS07157';
UPDATE users SET email = 'udayakumar.p@centrico.tech'    WHERE username = 'GBS04423';
