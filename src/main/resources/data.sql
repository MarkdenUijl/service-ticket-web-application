-- noinspection SqlDialectInspectionForFile

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
