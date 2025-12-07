-- noinspection SqlDialectInspectionForFile

/* PROJECTS DATA */
INSERT INTO projects (
    id,
    name,
    city,
    zip_code,
    street,
    house_number
)
VALUES
    (nextval('projects_seq'), 'Amsterdam Tower',          'Amsterdam', '1071 BV', 'Stadhouderskade',       123),
    (nextval('projects_seq'), 'Rotterdam Plaza',          'Rotterdam', '3012 CL', 'Coolsingel',            45),
    (nextval('projects_seq'), 'Utrecht Heights',          'Utrecht',   '3582 TZ', 'Maliebaan',             67),
    (nextval('projects_seq'), 'The Hague Residency',      'Den Haag',  '2597 AK', 'Scheveningseweg',       89),
    (nextval('projects_seq'), 'Groningen Central',        'Groningen', '9711 AA', 'Grote Markt',           10),
    (nextval('projects_seq'), 'Eindhoven Tower',          'Eindhoven', '5611 BB', 'Stratumseind',          34),
    (nextval('projects_seq'), 'Delft Tech Park',          'Delft',     '2628 CD', 'Mekelweg',              5),
    (nextval('projects_seq'), 'Leiden BioCenter',         'Leiden',    '2333 CA', 'Zernikedreef',          12),
    (nextval('projects_seq'), 'Arnhem Bridge Offices',    'Arnhem',    '6811 LG', 'John Frostbrug',        1),
    (nextval('projects_seq'), 'Haarlem HQ',               'Haarlem',   '2011 RD', 'Grote Markt',           3),
    (nextval('projects_seq'), 'Zwolle North',             'Zwolle',    '8011 NB', 'Melkmarkt',             88),
    (nextval('projects_seq'), 'Maastricht South',         'Maastricht','6211 LN', 'Vrijthof',              6),
    (nextval('projects_seq'), 'Tilburg Centrum',          'Tilburg',   '5038 EH', 'Heuvelstraat',          27),
    (nextval('projects_seq'), 'Breda Innovation Hub',     'Breda',     '4811 WE', 'Nieuwe Ginnekenstraat', 50),
    (nextval('projects_seq'), 'Almere Lakeview',          'Almere',    '1315 EZ', 'Wisselweg',             20),
    (nextval('projects_seq'), 'Apeldoorn Campus',         'Apeldoorn', '7311 KZ', 'Deventerstraat',        13),
    (nextval('projects_seq'), 'MECC',                     'Maastricht','6229 GV', 'Forum',                 100),
    (nextval('projects_seq'), 'Tergooi',                  'Hilversum', '1222 TK', 'Van Riebeeckweg',       4),
    (nextval('projects_seq'), 'Universiteit Delft',       'Delft',     '2628 CD', 'Mekelweg',              5);

/* SERVICE CONTRACT DATA */
INSERT INTO service_contracts (
    id,
    type,
    contract_time_in_minutes,
    used_time,
    start_date,
    end_date,
    project_id,
    previous_contract_id
)
VALUES
    -- Amsterdam Tower
    (nextval('service_contracts_seq'), 0, 480, 0,   '2023-01-01', '2023-12-31',
     (SELECT id FROM projects WHERE name = 'Amsterdam Tower'), NULL),

    -- Utrecht Heights
    (nextval('service_contracts_seq'), 1, 240, 30,  '2023-01-01', '2023-12-31',
     (SELECT id FROM projects WHERE name = 'Utrecht Heights'), NULL),

    -- Groningen Central
    (nextval('service_contracts_seq'), 0, 480, 15,  '2023-01-01', '2023-12-31',
     (SELECT id FROM projects WHERE name = 'Groningen Central'), NULL),

    -- Delft Tech Park
    (nextval('service_contracts_seq'), 1, 360, 120, '2024-01-01', '2024-12-31',
     (SELECT id FROM projects WHERE name = 'Delft Tech Park'), NULL),

    -- Leiden BioCenter
    (nextval('service_contracts_seq'), 0, 600, 200, '2024-03-01', '2025-02-28',
     (SELECT id FROM projects WHERE name = 'Leiden BioCenter'), NULL),

    -- Arnhem Bridge Offices
    (nextval('service_contracts_seq'), 1, 720, 250, '2023-07-01', '2024-06-30',
     (SELECT id FROM projects WHERE name = 'Arnhem Bridge Offices'), NULL),

    -- Haarlem HQ
    (nextval('service_contracts_seq'), 1, 300, 60,  '2024-05-01', '2025-04-30',
     (SELECT id FROM projects WHERE name = 'Haarlem HQ'), NULL),

    -- Zwolle North
    (nextval('service_contracts_seq'), 0, 900, 100, '2024-01-15', '2025-01-15',
     (SELECT id FROM projects WHERE name = 'Zwolle North'), NULL),

    -- Breda Innovation Hub
    (nextval('service_contracts_seq'), 0, 480, 100, '2024-02-01', '2025-01-31',
     (SELECT id FROM projects WHERE name = 'Breda Innovation Hub'), NULL),

    -- Tergooi
    (nextval('service_contracts_seq'), 1, 240, 80,  '2024-06-01', '2025-05-31',
     (SELECT id FROM projects WHERE name = 'Tergooi'), NULL),

    -- Universiteit Delft
    (nextval('service_contracts_seq'), 0, 360, 40,  '2023-11-01', '2024-10-31',
     (SELECT id FROM projects WHERE name = 'Universiteit Delft'), NULL),

    -- Apeldoorn Campus
    (nextval('service_contracts_seq'), 1, 600, 180, '2024-04-01', '2025-03-31',
     (SELECT id FROM projects WHERE name = 'Apeldoorn Campus'), NULL),

    -- Tilburg Centrum
    (nextval('service_contracts_seq'), 1, 480, 150, '2024-07-01', '2025-06-30',
     (SELECT id FROM projects WHERE name = 'Tilburg Centrum'), NULL);

/* PRIVILEGE DATA */
INSERT INTO privileges (id, name)
VALUES
    (nextval('privileges_seq'), 'CAN_SEE_CONTRACTS_PRIVILEGE'),
    (nextval('privileges_seq'), 'CAN_MODIFY_CONTRACTS_PRIVILEGE'),
    (nextval('privileges_seq'), 'CAN_SEE_PROJECTS_PRIVILEGE'),
    (nextval('privileges_seq'), 'CAN_MODIFY_PROJECTS_PRIVILEGE'),
    (nextval('privileges_seq'), 'CAN_SEE_USERS_PRIVILEGE'),
    (nextval('privileges_seq'), 'CAN_ACCESS_USERS_PRIVILEGE'),
    (nextval('privileges_seq'), 'CAN_MODIFY_USERS_PRIVILEGE'),
    (nextval('privileges_seq'), 'CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE'),
    (nextval('privileges_seq'), 'CAN_MAKE_ENGINEER_RESPONSE_PRIVILEGE'),
    (nextval('privileges_seq'), 'CAN_MODERATE_TICKET_RESPONSES_PRIVILEGE');

/* ROLE DATA */
INSERT INTO roles (id, name)
VALUES
    (nextval('roles_seq'), 'ROLE_ADMIN'),
    (nextval('roles_seq'), 'ROLE_ENGINEER'),
    (nextval('roles_seq'), 'ROLE_USER');

/* USER DATA */
INSERT INTO users (id, first_name, last_name, email, password, phone_number)
VALUES
    (nextval('users_seq'), 'Admin', 'Tester', 'admin@tester.nl', '$2a$10$EFG1ijoIuHicy2zpiqXvZ.qF.1kcxttcYz.znrI67el1rgmMkMj3W', '+31612345678'),
    (nextval('users_seq'), 'Engineer', 'Tester', 'engineer@tester.nl', '$2a$10$EFG1ijoIuHicy2zpiqXvZ.qF.1kcxttcYz.znrI67el1rgmMkMj3W', '+31612345678'),
    (nextval('users_seq'), 'User', 'Tester', 'user@tester.nl', '$2a$10$EFG1ijoIuHicy2zpiqXvZ.qF.1kcxttcYz.znrI67el1rgmMkMj3W', '+31612345678');

/* SUB TABLES DATA */
INSERT INTO roles_privileges (role_id, privilege_id)
VALUES
    ((SELECT id FROM roles WHERE name = 'ROLE_USER'), (SELECT id FROM privileges WHERE name = 'CAN_SEE_USERS_PRIVILEGE')),
    ((SELECT id FROM roles WHERE name = 'ROLE_ENGINEER'), (SELECT id FROM privileges WHERE name = 'CAN_SEE_USERS_PRIVILEGE')),
    ((SELECT id FROM roles WHERE name = 'ROLE_ENGINEER'), (SELECT id FROM privileges WHERE name = 'CAN_ACCESS_USERS_PRIVILEGE')),
    ((SELECT id FROM roles WHERE name = 'ROLE_ENGINEER'), (SELECT id FROM privileges WHERE name = 'CAN_SEE_CONTRACTS_PRIVILEGE')),
    ((SELECT id FROM roles WHERE name = 'ROLE_ENGINEER'), (SELECT id FROM privileges WHERE name = 'CAN_SEE_PROJECTS_PRIVILEGE')),
    ((SELECT id FROM roles WHERE name = 'ROLE_ENGINEER'), (SELECT id FROM privileges WHERE name = 'CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE')),
    ((SELECT id FROM roles WHERE name = 'ROLE_ENGINEER'), (SELECT id FROM privileges WHERE name = 'CAN_MAKE_ENGINEER_RESPONSE_PRIVILEGE')),
    ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_SEE_USERS_PRIVILEGE')),
    ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_ACCESS_USERS_PRIVILEGE')),
    ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_MODIFY_USERS_PRIVILEGE')),
    ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_SEE_CONTRACTS_PRIVILEGE')),
    ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_MODIFY_CONTRACTS_PRIVILEGE')),
    ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_SEE_PROJECTS_PRIVILEGE')),
    ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_MODIFY_PROJECTS_PRIVILEGE')),
    ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE')),
    ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_MAKE_ENGINEER_RESPONSE_PRIVILEGE')),
    ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_MODERATE_TICKET_RESPONSES_PRIVILEGE'));

INSERT INTO users_roles (user_id, role_id)
VALUES
    ((SELECT id FROM users WHERE email = 'admin@tester.nl'), (SELECT id FROM roles WHERE name = 'ROLE_ADMIN')),
    ((SELECT id FROM users WHERE email = 'engineer@tester.nl'), (SELECT id FROM roles WHERE name = 'ROLE_ENGINEER')),
    ((SELECT id FROM users WHERE email = 'user@tester.nl'), (SELECT id FROM roles WHERE name = 'ROLE_USER'));


/* LINK CURRENT CONTRACTS */
UPDATE projects p
SET current_contract_id = c.id
    FROM service_contracts c
WHERE c.project_id = p.id
  AND c.previous_contract_id IS NULL;     (SELECT COALESCE(MAX(id), 1) FROM projects));