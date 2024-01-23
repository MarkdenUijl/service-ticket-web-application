-- noinspection SqlDialectInspectionForFile

/* SERVICE CONTRACT DATA */
INSERT INTO service_contracts (id, type, contract_time_in_minutes, used_time, start_date, end_date)
VALUES
    (nextval('service_contracts_seq'), 0, 480, 0, '2023-01-01', '2023-12-31'),
    (nextval('service_contracts_seq'), 1, 240, 30, '2023-01-01', '2023-12-31'),
    (nextval('service_contracts_seq'), 2, 480, 15, '2023-01-01', '2023-12-31');

/* PROJECTS DATA */
INSERT INTO projects (id, name, city, zip_code, street, house_number, service_contract_id)
VALUES
    (nextval('projects_seq'), 'Amsterdam Tower', 'Amsterdam', '1071 BV', 'Stadhouderskade', 123, 1),
    (nextval('projects_seq'), 'Rotterdam Plaza', 'Rotterdam', '3012 CL', 'Coolsingel', 45, null),
    (nextval('projects_seq'), 'Utrecht Heights', 'Utrecht', '3582 TZ', 'Maliebaan', 67, 51),
    (nextval('projects_seq'), 'The Hague Residency', 'Den Haag', '2597 AK', 'Scheveningseweg', 89, null),
    (nextval('projects_seq'), 'Groningen Central', 'Groningen', '9711 AA', 'Grote Markt', 10, 101),
    (nextval('projects_seq'), 'Eindhoven Tower', 'Eindhoven', '5611 BB', 'Stratumseind', 34, null);


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

/* SERVICE TICKETS DATA */
INSERT INTO service_tickets (id, name, status, type, description, project_id, minutes_spent, creation_date, user_id)
VALUES
    (nextval('service_tickets_seq'), 'Issue with Server', 0, 0, 'Experiencing connectivity problems', 1, 0, '2023-06-28T09:00:00', 101),
    (nextval('service_tickets_seq'), 'Network Latency', 1, 1, 'Experiencing slow internet speeds', 51, 0, '2023-09-02T10:30:00', 101),
    (nextval('service_tickets_seq'), 'Software Installation', 1, 1, 'Request for software setup on workstation', 101, 0, '2024-01-01T11:45:00', 101),
    (nextval('service_tickets_seq'), 'Email Configuration Issue', 2, 0, 'Unable to send/receive emails', 151, 0, '2024-01-06T13:15:00', 101);

/* SERVICE TICKET RESPONSE DATA */
INSERT INTO ticket_responses (id, response_type, response, creation_date, ticket_id, minutes_spent, user_id)
VALUES
    (nextval('ticket_responses_seq'), 'engineer_response', 'Tried rebooting the router', '2023-12-28T09:10:00', 1, 2, 51),
    (nextval('ticket_responses_seq'), 'basic_response', 'Performed speed test, investigating the issue', '2024-01-02T10:35:00', 51, null, 101),
    (nextval('ticket_responses_seq'), 'engineer_response', 'Identified high latency due to ISP issue, contacting ISP support', '2024-01-02T11:00:00', 51, 4, 51),
    (nextval('ticket_responses_seq'), 'basic_response', 'ISP confirmed issue in the area, estimated resolution time provided', '2024-01-02T11:30:00', 51, null, 101),
    (nextval('ticket_responses_seq'), 'engineer_response', 'Contacting user to gather software requirements', '2024-01-02T11:50:00', 101, 3, 51),
    (nextval('ticket_responses_seq'), 'basic_response', 'Received software specifications, initiating installation', '2024-01-02T12:15:00', 101, null, 101),
    (nextval('ticket_responses_seq'), 'engineer_response', 'Software installation completed successfully', '2024-01-02T13:00:00', 101, 6, 51),
    (nextval('ticket_responses_seq'), 'basic_response', 'Checking mail server settings for possible issues', '2024-01-02T13:20:00', 151, null, 101),
    (nextval('ticket_responses_seq'), 'engineer_response', 'Adjusted SMTP settings, testing outgoing mail', '2024-01-02T13:40:00', 151, 7, 51),
    (nextval('ticket_responses_seq'), 'basic_response', 'Incoming mail settings updated, testing incoming mail', '2024-01-02T14:00:00', 151, null, 101);


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
