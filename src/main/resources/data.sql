-- noinspection SqlDialectInspectionForFile

/* SERVICE CONTRACT DATA */
INSERT INTO service_contracts (id, type, contract_time_in_minutes, used_time, start_date, end_date)
VALUES
    (nextval('service_contracts_seq'), 0, 480, 0, '2023-01-01', '2023-12-31'),
    (nextval('service_contracts_seq'), 1, 240, 30, '2023-01-01', '2023-12-31'),
    (nextval('service_contracts_seq'), 0, 480, 15, '2023-01-01', '2023-12-31'),
    (nextval('service_contracts_seq'), 1, 360, 120, '2024-01-01', '2024-12-31'),
    (nextval('service_contracts_seq'), 0, 600, 200, '2024-03-01', '2025-02-28'),
    (nextval('service_contracts_seq'), 1, 720, 250, '2023-07-01', '2024-06-30'),
    (nextval('service_contracts_seq'), 1, 300, 60, '2024-05-01', '2025-04-30'),
    (nextval('service_contracts_seq'), 0, 900, 100, '2024-01-15', '2025-01-15'),
    (nextval('service_contracts_seq'), 0, 480, 100, '2024-02-01', '2025-01-31'),
    (nextval('service_contracts_seq'), 1, 240, 80, '2024-06-01', '2025-05-31'),
    (nextval('service_contracts_seq'), 0, 360, 40, '2023-11-01', '2024-10-31'),
    (nextval('service_contracts_seq'), 1, 600, 180, '2024-04-01', '2025-03-31'),
    (nextval('service_contracts_seq'), 1, 480, 150, '2024-07-01', '2025-06-30');

/* PROJECTS DATA */
INSERT INTO projects (id, name, city, zip_code, street, house_number, service_contract_id)
VALUES
    (nextval('projects_seq'), 'Amsterdam Tower', 'Amsterdam', '1071 BV', 'Stadhouderskade', 123, 1),
    (nextval('projects_seq'), 'Rotterdam Plaza', 'Rotterdam', '3012 CL', 'Coolsingel', 45, null),
    (nextval('projects_seq'), 'Utrecht Heights', 'Utrecht', '3582 TZ', 'Maliebaan', 67, 51),
    (nextval('projects_seq'), 'The Hague Residency', 'Den Haag', '2597 AK', 'Scheveningseweg', 89, null),
    (nextval('projects_seq'), 'Groningen Central', 'Groningen', '9711 AA', 'Grote Markt', 10, 101),
    (nextval('projects_seq'), 'Eindhoven Tower', 'Eindhoven', '5611 BB', 'Stratumseind', 34, null),
    (nextval('projects_seq'), 'Delft Tech Park', 'Delft', '2628 CD', 'Mekelweg', 5, 151),
    (nextval('projects_seq'), 'Leiden BioCenter', 'Leiden', '2333 CA', 'Zernikedreef', 12, 201),
    (nextval('projects_seq'), 'Arnhem Bridge Offices', 'Arnhem', '6811 LG', 'John Frostbrug', 1, 251),
    (nextval('projects_seq'), 'Haarlem HQ', 'Haarlem', '2011 RD', 'Grote Markt', 3, 301),
    (nextval('projects_seq'), 'Zwolle North', 'Zwolle', '8011 NB', 'Melkmarkt', 88, 351),
    (nextval('projects_seq'), 'Maastricht South', 'Maastricht', '6211 LN', 'Vrijthof', 6, null),
    (nextval('projects_seq'), 'Tilburg Centrum', 'Tilburg', '5038 EH', 'Heuvelstraat', 27, 401),
    (nextval('projects_seq'), 'Breda Innovation Hub', 'Breda', '4811 WE', 'Nieuwe Ginnekenstraat', 50, 451),
    (nextval('projects_seq'), 'Almere Lakeview', 'Almere', '1315 EZ', 'Wisselweg', 20, null),
    (nextval('projects_seq'), 'Apeldoorn Campus', 'Apeldoorn', '7311 KZ', 'Deventerstraat', 13, 501),
    (nextval('projects_seq'), 'MECC', 'Maastricht', '6229 GV', 'Forum', 100, null),
    (nextval('projects_seq'), 'Tergooi', 'Hilversum', '1222 TK', 'Van Riebeeckweg', 4, 551),
    (nextval('projects_seq'), 'Universiteit Delft', 'Delft', '2628 CD', 'Mekelweg', 5, 601);

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
-- INSERT INTO service_tickets (id, name, status, type, description, project_id, minutes_spent, creation_date, user_id)
-- VALUES
--     (nextval('service_tickets_seq'), 'Issue with Server', 0, 0, 'Experiencing connectivity problems', 1, 0, '2023-06-28T09:00:00', 101),
--     (nextval('service_tickets_seq'), 'Network Latency', 1, 1, 'Experiencing slow internet speeds', 51, 0, '2023-09-02T10:30:00', 101),
--     (nextval('service_tickets_seq'), 'Software Installation', 1, 1, 'Request for software setup on workstation', 101, 0, '2024-01-01T11:45:00', 101),
--     (nextval('service_tickets_seq'), 'Email Configuration Issue', 2, 0, 'Unable to send/receive emails', 151, 0, '2024-01-06T13:15:00', 101),
--     (nextval('service_tickets_seq'), 'Database Migration Request', 0, 1, 'Need to migrate PostgreSQL DB', 201, 0, '2024-03-10T10:00:00', 101),
--     (nextval('service_tickets_seq'), 'VPN Connection Issue', 1, 0, 'VPN not connecting for remote users', 251, 0, '2024-04-05T14:45:00', 101),
--     (nextval('service_tickets_seq'), 'Firewall Rules Update', 2, 1, 'Request to update outgoing rules', 301, 0, '2024-05-12T16:30:00', 101),
--     (nextval('service_tickets_seq'), 'Printer Not Responding', 0, 0, 'Office printer stuck in queue', 351, 0, '2024-05-21T09:20:00', 101),
--     (nextval('service_tickets_seq'), 'Password Reset Request', 1, 1, 'User cannot log in to portal', 401, 0, '2024-06-01T08:00:00', 101),
--     (nextval('service_tickets_seq'), 'Hardware Replacement', 2, 1, 'Old workstation needs replacement', 451, 0, '2024-06-18T13:30:00', 101),
--     (nextval('service_tickets_seq'), 'Data Loss Incident', 0, 0, 'Files disappeared from shared drive', 501, 0, '2024-07-03T11:15:00', 101),
--     (nextval('service_tickets_seq'), 'Security Patch Deployment', 1, 1, 'Urgent update for critical CVE', 551, 0, '2024-07-25T15:40:00', 101),
--     (nextval('service_tickets_seq'), 'Mobile App Crash', 0, 0, 'Crash after login on Android devices', 601, 0, '2024-08-01T17:00:00', 101),
--     (nextval('service_tickets_seq'), 'Wi-Fi Downtime', 2, 1, 'Office Wi-Fi inaccessible intermittently', 601, 0, '2024-08-05T08:45:00', 101);
--
-- /* SERVICE TICKET RESPONSE DATA */
-- INSERT INTO ticket_responses (id, response_type, response, creation_date, ticket_id, minutes_spent, user_id)
-- VALUES
--     (nextval('ticket_responses_seq'), 'engineer_response', 'Tried rebooting the router', '2023-12-28T09:10:00', 1, 2, 51),
--     (nextval('ticket_responses_seq'), 'basic_response', 'Performed speed test, investigating the issue', '2024-01-02T10:35:00', 51, null, 101),
--     (nextval('ticket_responses_seq'), 'engineer_response', 'Identified high latency due to ISP issue, contacting ISP support', '2024-01-02T11:00:00', 51, 4, 51),
--     (nextval('ticket_responses_seq'), 'basic_response', 'ISP confirmed issue in the area, estimated resolution time provided', '2024-01-02T11:30:00', 51, null, 101),
--     (nextval('ticket_responses_seq'), 'engineer_response', 'Contacting user to gather software requirements', '2024-01-02T11:50:00', 101, 3, 51),
--     (nextval('ticket_responses_seq'), 'basic_response', 'Received software specifications, initiating installation', '2024-01-02T12:15:00', 101, null, 101),
--     (nextval('ticket_responses_seq'), 'engineer_response', 'Software installation completed successfully', '2024-01-02T13:00:00', 101, 6, 51),
--     (nextval('ticket_responses_seq'), 'basic_response', 'Checking mail server settings for possible issues', '2024-01-02T13:20:00', 151, null, 101),
--     (nextval('ticket_responses_seq'), 'engineer_response', 'Adjusted SMTP settings, testing outgoing mail', '2024-01-02T13:40:00', 151, 7, 51),
--     (nextval('ticket_responses_seq'), 'basic_response', 'Incoming mail settings updated, testing incoming mail', '2024-01-02T14:00:00', 151, null, 101),
--     (nextval('ticket_responses_seq'), 'engineer_response', 'Initiated PostgreSQL dump backup', '2024-03-10T10:15:00', 201, 4, 51),
--     (nextval('ticket_responses_seq'), 'basic_response', 'Backup completed, starting migration', '2024-03-10T10:45:00', 201, null, 101),
--     (nextval('ticket_responses_seq'), 'engineer_response', 'VPN gateway config reset', '2024-04-05T15:00:00', 251, 2, 51),
--     (nextval('ticket_responses_seq'), 'basic_response', 'Tested from client side, still failing', '2024-04-05T15:20:00', 251, null, 101),
--     (nextval('ticket_responses_seq'), 'engineer_response', 'Firewall rules updated for port 443', '2024-05-12T17:00:00', 301, 3, 51),
--     (nextval('ticket_responses_seq'), 'basic_response', 'Confirmed remote access now working', '2024-05-12T17:15:00', 301, null, 101),
--     (nextval('ticket_responses_seq'), 'engineer_response', 'Printer restarted, job flushed', '2024-05-21T09:40:00', 351, 1, 51),
--     (nextval('ticket_responses_seq'), 'basic_response', 'Printed test document successfully', '2024-05-21T09:50:00', 351, null, 101),
--     (nextval('ticket_responses_seq'), 'engineer_response', 'Password reset token generated', '2024-06-01T08:15:00', 401, 1, 51),
--     (nextval('ticket_responses_seq'), 'basic_response', 'Password changed and working', '2024-06-01T08:25:00', 401, null, 101);


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



-- noinspection SqlDialectInspectionForFile

-- /* SERVICE CONTRACT DATA */
-- INSERT INTO service_contracts (id, type, contract_time_in_minutes, used_time, start_date, end_date)
-- VALUES
--     (nextval('service_contracts_seq'), 0, 480, 0, '2023-01-01', '2023-12-31'),
--     (nextval('service_contracts_seq'), 1, 240, 30, '2023-01-01', '2023-12-31'),
--     (nextval('service_contracts_seq'), 0, 480, 15, '2023-01-01', '2023-12-31'),
--     (nextval('service_contracts_seq'), 1, 360, 120, '2024-01-01', '2024-12-31'),
--     (nextval('service_contracts_seq'), 0, 600, 200, '2024-03-01', '2025-02-28'),
--     (nextval('service_contracts_seq'), 1, 720, 250, '2023-07-01', '2024-06-30'),
--     (nextval('service_contracts_seq'), 1, 300, 60, '2024-05-01', '2025-04-30'),
--     (nextval('service_contracts_seq'), 0, 900, 100, '2024-01-15', '2025-01-15'),
--     (nextval('service_contracts_seq'), 0, 480, 100, '2024-02-01', '2025-01-31'),
--     (nextval('service_contracts_seq'), 1, 240, 80, '2024-06-01', '2025-05-31'),
--     (nextval('service_contracts_seq'), 0, 360, 40, '2023-11-01', '2024-10-31'),
--     (nextval('service_contracts_seq'), 1, 600, 180, '2024-04-01', '2025-03-31'),
--     (nextval('service_contracts_seq'), 1, 480, 150, '2024-07-01', '2025-06-30');
--
-- /* PROJECTS DATA */
-- INSERT INTO projects (id, name, city, zip_code, street, house_number, service_contract_id)
-- VALUES
--     (nextval('projects_seq'), 'Amsterdam Tower', 'Amsterdam', '1071 BV', 'Stadhouderskade', 123, (SELECT id FROM service_contracts WHERE type = 0 AND contract_time_in_minutes = 480 AND used_time = 0 AND start_date = '2023-01-01' AND end_date = '2023-12-31' LIMIT 1)),
--     (nextval('projects_seq'), 'Rotterdam Plaza', 'Rotterdam', '3012 CL', 'Coolsingel', 45, null),
--     (nextval('projects_seq'), 'Utrecht Heights', 'Utrecht', '3582 TZ', 'Maliebaan', 67, (SELECT id FROM service_contracts WHERE type = 1 AND contract_time_in_minutes = 240 AND used_time = 30 AND start_date = '2023-01-01' AND end_date = '2023-12-31' LIMIT 1)),
--     (nextval('projects_seq'), 'The Hague Residency', 'Den Haag', '2597 AK', 'Scheveningseweg', 89, null),
--     (nextval('projects_seq'), 'Groningen Central', 'Groningen', '9711 AA', 'Grote Markt', 10, (SELECT id FROM service_contracts WHERE type = 0 AND contract_time_in_minutes = 480 AND used_time = 15 AND start_date = '2023-01-01' AND end_date = '2023-12-31' LIMIT 1)),
--     (nextval('projects_seq'), 'Eindhoven Tower', 'Eindhoven', '5611 BB', 'Stratumseind', 34, null),
--     (nextval('projects_seq'), 'Delft Tech Park', 'Delft', '2628 CD', 'Mekelweg', 5, (SELECT id FROM service_contracts WHERE type = 1 AND contract_time_in_minutes = 360 AND used_time = 120 AND start_date = '2024-01-01' AND end_date = '2024-12-31' LIMIT 1)),
--     (nextval('projects_seq'), 'Leiden BioCenter', 'Leiden', '2333 CA', 'Zernikedreef', 12, (SELECT id FROM service_contracts WHERE type = 0 AND contract_time_in_minutes = 600 AND used_time = 200 AND start_date = '2024-03-01' AND end_date = '2025-02-28' LIMIT 1)),
--     (nextval('projects_seq'), 'Arnhem Bridge Offices', 'Arnhem', '6811 LG', 'John Frostbrug', 1, (SELECT id FROM service_contracts WHERE type = 1 AND contract_time_in_minutes = 720 AND used_time = 250 AND start_date = '2023-07-01' AND end_date = '2024-06-30' LIMIT 1)),
--     (nextval('projects_seq'), 'Haarlem HQ', 'Haarlem', '2011 RD', 'Grote Markt', 3, (SELECT id FROM service_contracts WHERE type = 1 AND contract_time_in_minutes = 300 AND used_time = 60 AND start_date = '2024-05-01' AND end_date = '2025-04-30' LIMIT 1)),
--     (nextval('projects_seq'), 'Zwolle North', 'Zwolle', '8011 NB', 'Melkmarkt', 88, (SELECT id FROM service_contracts WHERE type = 0 AND contract_time_in_minutes = 900 AND used_time = 100 AND start_date = '2024-01-15' AND end_date = '2025-01-15' LIMIT 1)),
--     (nextval('projects_seq'), 'Maastricht South', 'Maastricht', '6211 LN', 'Vrijthof', 6, null),
--     (nextval('projects_seq'), 'Tilburg Centrum', 'Tilburg', '5038 EH', 'Heuvelstraat', 27, (SELECT id FROM service_contracts WHERE type = 1 AND contract_time_in_minutes = 480 AND used_time = 150 AND start_date = '2024-07-01' AND end_date = '2025-06-30' LIMIT 1)),
--     (nextval('projects_seq'), 'Breda Innovation Hub', 'Breda', '4811 WE', 'Nieuwe Ginnekenstraat', 50, (SELECT id FROM service_contracts WHERE type = 0 AND contract_time_in_minutes = 480 AND used_time = 100 AND start_date = '2024-02-01' AND end_date = '2025-01-31' LIMIT 1)),
--     (nextval('projects_seq'), 'Almere Lakeview', 'Almere', '1315 EZ', 'Wisselweg', 20, null),
--     (nextval('projects_seq'), 'Apeldoorn Campus', 'Apeldoorn', '7311 KZ', 'Deventerstraat', 13, (SELECT id FROM service_contracts WHERE type = 1 AND contract_time_in_minutes = 600 AND used_time = 180 AND start_date = '2024-04-01' AND end_date = '2025-03-31' LIMIT 1)),
--     (nextval('projects_seq'), 'MECC', 'Maastricht', '6229 GV', 'Forum', 100, null),
--     (nextval('projects_seq'), 'Tergooi', 'Hilversum', '1222 TK', 'Van Riebeeckweg', 4, (SELECT id FROM service_contracts WHERE type = 1 AND contract_time_in_minutes = 240 AND used_time = 80 AND start_date = '2024-06-01' AND end_date = '2025-05-31' LIMIT 1)),
--     (nextval('projects_seq'), 'Universiteit Delft', 'Delft', '2628 CD', 'Mekelweg', 5, (SELECT id FROM service_contracts WHERE type = 0 AND contract_time_in_minutes = 360 AND used_time = 40 AND start_date = '2023-11-01' AND end_date = '2024-10-31' LIMIT 1));
--
-- /* PRIVILEGE DATA */
-- INSERT INTO privileges (id, name)
-- VALUES
--     (nextval('privileges_seq'), 'CAN_SEE_CONTRACTS_PRIVILEGE'),
--     (nextval('privileges_seq'), 'CAN_MODIFY_CONTRACTS_PRIVILEGE'),
--     (nextval('privileges_seq'), 'CAN_SEE_PROJECTS_PRIVILEGE'),
--     (nextval('privileges_seq'), 'CAN_MODIFY_PROJECTS_PRIVILEGE'),
--     (nextval('privileges_seq'), 'CAN_SEE_USERS_PRIVILEGE'),
--     (nextval('privileges_seq'), 'CAN_ACCESS_USERS_PRIVILEGE'),
--     (nextval('privileges_seq'), 'CAN_MODIFY_USERS_PRIVILEGE'),
--     (nextval('privileges_seq'), 'CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE'),
--     (nextval('privileges_seq'), 'CAN_MAKE_ENGINEER_RESPONSE_PRIVILEGE'),
--     (nextval('privileges_seq'), 'CAN_MODERATE_TICKET_RESPONSES_PRIVILEGE');
--
-- /* ROLE DATA */
-- INSERT INTO roles (id, name)
-- VALUES
--     (nextval('roles_seq'), 'ROLE_ADMIN'),
--     (nextval('roles_seq'), 'ROLE_ENGINEER'),
--     (nextval('roles_seq'), 'ROLE_USER');
--
-- /* USER DATA */
-- INSERT INTO users (id, first_name, last_name, email, password, phone_number)
-- VALUES
--     (nextval('users_seq'), 'Admin', 'Tester', 'admin@tester.nl', '$2a$10$EFG1ijoIuHicy2zpiqXvZ.qF.1kcxttcYz.znrI67el1rgmMkMj3W', '+31612345678'),
--     (nextval('users_seq'), 'Engineer', 'Tester', 'engineer@tester.nl', '$2a$10$EFG1ijoIuHicy2zpiqXvZ.qF.1kcxttcYz.znrI67el1rgmMkMj3W', '+31612345678'),
--     (nextval('users_seq'), 'User', 'Tester', 'user@tester.nl', '$2a$10$EFG1ijoIuHicy2zpiqXvZ.qF.1kcxttcYz.znrI67el1rgmMkMj3W', '+31612345678');
--
-- /* SERVICE TICKETS DATA */
-- INSERT INTO service_tickets (id, name, status, type, description, project_id, minutes_spent, creation_date, user_id)
-- VALUES
--     (nextval('service_tickets_seq'), 'Issue with Server', 0, 0, 'Experiencing connectivity problems', (SELECT id FROM projects WHERE name = 'Amsterdam Tower' LIMIT 1), 0, '2023-06-28T09:00:00', (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('service_tickets_seq'), 'Network Latency', 1, 1, 'Experiencing slow internet speeds', (SELECT id FROM projects WHERE name = 'Utrecht Heights' LIMIT 1), 0, '2023-09-02T10:30:00', (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('service_tickets_seq'), 'Software Installation', 1, 1, 'Request for software setup on workstation', (SELECT id FROM projects WHERE name = 'Groningen Central' LIMIT 1), 0, '2024-01-01T11:45:00', (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('service_tickets_seq'), 'Email Configuration Issue', 2, 0, 'Unable to send/receive emails', (SELECT id FROM projects WHERE name = 'Delft Tech Park' LIMIT 1), 0, '2024-01-06T13:15:00', (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('service_tickets_seq'), 'Database Migration Request', 0, 1, 'Need to migrate PostgreSQL DB', (SELECT id FROM projects WHERE name = 'Leiden BioCenter' LIMIT 1), 0, '2024-03-10T10:00:00', (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('service_tickets_seq'), 'VPN Connection Issue', 1, 0, 'VPN not connecting for remote users', (SELECT id FROM projects WHERE name = 'Arnhem Bridge Offices' LIMIT 1), 0, '2024-04-05T14:45:00', (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('service_tickets_seq'), 'Firewall Rules Update', 2, 1, 'Request to update outgoing rules', (SELECT id FROM projects WHERE name = 'Haarlem HQ' LIMIT 1), 0, '2024-05-12T16:30:00', (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('service_tickets_seq'), 'Printer Not Responding', 0, 0, 'Office printer stuck in queue', (SELECT id FROM projects WHERE name = 'Zwolle North' LIMIT 1), 0, '2024-05-21T09:20:00', (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('service_tickets_seq'), 'Password Reset Request', 1, 1, 'User cannot log in to portal', (SELECT id FROM projects WHERE name = 'Tilburg Centrum' LIMIT 1), 0, '2024-06-01T08:00:00', (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('service_tickets_seq'), 'Hardware Replacement', 2, 1, 'Old workstation needs replacement', (SELECT id FROM projects WHERE name = 'Breda Innovation Hub' LIMIT 1), 0, '2024-06-18T13:30:00', (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('service_tickets_seq'), 'Data Loss Incident', 0, 0, 'Files disappeared from shared drive', (SELECT id FROM projects WHERE name = 'Apeldoorn Campus' LIMIT 1), 0, '2024-07-03T11:15:00', (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('service_tickets_seq'), 'Security Patch Deployment', 1, 1, 'Urgent update for critical CVE', (SELECT id FROM projects WHERE name = 'Tergooi' LIMIT 1), 0, '2024-07-25T15:40:00', (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('service_tickets_seq'), 'Mobile App Crash', 0, 0, 'Crash after login on Android devices', (SELECT id FROM projects WHERE name = 'Universiteit Delft' LIMIT 1), 0, '2024-08-01T17:00:00', (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('service_tickets_seq'), 'Wi-Fi Downtime', 2, 1, 'Office Wi-Fi inaccessible intermittently', (SELECT id FROM projects WHERE name = 'Universiteit Delft' LIMIT 1), 0, '2024-08-05T08:45:00', (SELECT id FROM users WHERE email = 'user@tester.nl'));
--
-- /* SERVICE TICKET RESPONSE DATA */
-- INSERT INTO ticket_responses (id, response_type, response, creation_date, ticket_id, minutes_spent, user_id)
-- VALUES
--     (nextval('ticket_responses_seq'), 'engineer_response', 'Tried rebooting the router', '2023-12-28T09:10:00',
--      (SELECT id FROM service_tickets WHERE name = 'Issue with Server' LIMIT 1), 2,
--     (SELECT id FROM users WHERE email = 'engineer@tester.nl')),
--     (nextval('ticket_responses_seq'), 'basic_response', 'Performed speed test, investigating the issue', '2024-01-02T10:35:00',
--         (SELECT id FROM service_tickets WHERE name = 'Network Latency' LIMIT 1), null,
--         (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('ticket_responses_seq'), 'engineer_response', 'Identified high latency due to ISP issue, contacting ISP support', '2024-01-02T11:00:00',
--         (SELECT id FROM service_tickets WHERE name = 'Network Latency' LIMIT 1), 4,
--         (SELECT id FROM users WHERE email = 'engineer@tester.nl')),
--     (nextval('ticket_responses_seq'), 'basic_response', 'ISP confirmed issue in the area, estimated resolution time provided', '2024-01-02T11:30:00',
--         (SELECT id FROM service_tickets WHERE name = 'Network Latency' LIMIT 1), null,
--         (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('ticket_responses_seq'), 'engineer_response', 'Contacting user to gather software requirements', '2024-01-02T11:50:00',
--         (SELECT id FROM service_tickets WHERE name = 'Software Installation' LIMIT 1), 3,
--         (SELECT id FROM users WHERE email = 'engineer@tester.nl')),
--     (nextval('ticket_responses_seq'), 'basic_response', 'Received software specifications, initiating installation', '2024-01-02T12:15:00',
--         (SELECT id FROM service_tickets WHERE name = 'Software Installation' LIMIT 1), null,
--         (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('ticket_responses_seq'), 'engineer_response', 'Software installation completed successfully', '2024-01-02T13:00:00',
--         (SELECT id FROM service_tickets WHERE name = 'Software Installation' LIMIT 1), 6,
--         (SELECT id FROM users WHERE email = 'engineer@tester.nl')),
--     (nextval('ticket_responses_seq'), 'basic_response', 'Checking mail server settings for possible issues', '2024-01-02T13:20:00',
--         (SELECT id FROM service_tickets WHERE name = 'Email Configuration Issue' LIMIT 1), null,
--         (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('ticket_responses_seq'), 'engineer_response', 'Adjusted SMTP settings, testing outgoing mail', '2024-01-02T13:40:00',
--         (SELECT id FROM service_tickets WHERE name = 'Email Configuration Issue' LIMIT 1), 7,
--         (SELECT id FROM users WHERE email = 'engineer@tester.nl')),
--     (nextval('ticket_responses_seq'), 'basic_response', 'Incoming mail settings updated, testing incoming mail', '2024-01-02T14:00:00',
--         (SELECT id FROM service_tickets WHERE name = 'Email Configuration Issue' LIMIT 1), null,
--         (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('ticket_responses_seq'), 'engineer_response', 'Initiated PostgreSQL dump backup', '2024-03-10T10:15:00',
--         (SELECT id FROM service_tickets WHERE name = 'Database Migration Request' LIMIT 1), 4,
--         (SELECT id FROM users WHERE email = 'engineer@tester.nl')),
--     (nextval('ticket_responses_seq'), 'basic_response', 'Backup completed, starting migration', '2024-03-10T10:45:00',
--         (SELECT id FROM service_tickets WHERE name = 'Database Migration Request' LIMIT 1), null,
--         (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('ticket_responses_seq'), 'engineer_response', 'VPN gateway config reset', '2024-04-05T15:00:00',
--         (SELECT id FROM service_tickets WHERE name = 'VPN Connection Issue' LIMIT 1), 2,
--         (SELECT id FROM users WHERE email = 'engineer@tester.nl')),
--     (nextval('ticket_responses_seq'), 'basic_response', 'Tested from client side, still failing', '2024-04-05T15:20:00',
--         (SELECT id FROM service_tickets WHERE name = 'VPN Connection Issue' LIMIT 1), null,
--         (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('ticket_responses_seq'), 'engineer_response', 'Firewall rules updated for port 443', '2024-05-12T17:00:00',
--         (SELECT id FROM service_tickets WHERE name = 'Firewall Rules Update' LIMIT 1), 3,
--         (SELECT id FROM users WHERE email = 'engineer@tester.nl')),
--     (nextval('ticket_responses_seq'), 'basic_response', 'Confirmed remote access now working', '2024-05-12T17:15:00',
--         (SELECT id FROM service_tickets WHERE name = 'Firewall Rules Update' LIMIT 1), null,
--         (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('ticket_responses_seq'), 'engineer_response', 'Printer restarted, job flushed', '2024-05-21T09:40:00',
--         (SELECT id FROM service_tickets WHERE name = 'Printer Not Responding' LIMIT 1), 1,
--         (SELECT id FROM users WHERE email = 'engineer@tester.nl')),
--     (nextval('ticket_responses_seq'), 'basic_response', 'Printed test document successfully', '2024-05-21T09:50:00',
--         (SELECT id FROM service_tickets WHERE name = 'Printer Not Responding' LIMIT 1), null,
--         (SELECT id FROM users WHERE email = 'user@tester.nl')),
--     (nextval('ticket_responses_seq'), 'engineer_response', 'Password reset token generated', '2024-06-01T08:15:00',
--         (SELECT id FROM service_tickets WHERE name = 'Password Reset Request' LIMIT 1), 1,
--         (SELECT id FROM users WHERE email = 'engineer@tester.nl')),
--     (nextval('ticket_responses_seq'), 'basic_response', 'Password changed and working', '2024-06-01T08:25:00',
--         (SELECT id FROM service_tickets WHERE name = 'Password Reset Request' LIMIT 1), null,
--         (SELECT id FROM users WHERE email = 'user@tester.nl'));
--
--
-- /* SUB TABLES DATA */
-- INSERT INTO roles_privileges (role_id, privilege_id)
-- VALUES
--     ((SELECT id FROM roles WHERE name = 'ROLE_USER'), (SELECT id FROM privileges WHERE name = 'CAN_SEE_USERS_PRIVILEGE')),
--     ((SELECT id FROM roles WHERE name = 'ROLE_ENGINEER'), (SELECT id FROM privileges WHERE name = 'CAN_SEE_USERS_PRIVILEGE')),
--     ((SELECT id FROM roles WHERE name = 'ROLE_ENGINEER'), (SELECT id FROM privileges WHERE name = 'CAN_ACCESS_USERS_PRIVILEGE')),
--     ((SELECT id FROM roles WHERE name = 'ROLE_ENGINEER'), (SELECT id FROM privileges WHERE name = 'CAN_SEE_CONTRACTS_PRIVILEGE')),
--     ((SELECT id FROM roles WHERE name = 'ROLE_ENGINEER'), (SELECT id FROM privileges WHERE name = 'CAN_SEE_PROJECTS_PRIVILEGE')),
--     ((SELECT id FROM roles WHERE name = 'ROLE_ENGINEER'), (SELECT id FROM privileges WHERE name = 'CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE')),
--     ((SELECT id FROM roles WHERE name = 'ROLE_ENGINEER'), (SELECT id FROM privileges WHERE name = 'CAN_MAKE_ENGINEER_RESPONSE_PRIVILEGE')),
--     ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_SEE_USERS_PRIVILEGE')),
--     ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_ACCESS_USERS_PRIVILEGE')),
--     ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_MODIFY_USERS_PRIVILEGE')),
--     ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_SEE_CONTRACTS_PRIVILEGE')),
--     ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_MODIFY_CONTRACTS_PRIVILEGE')),
--     ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_SEE_PROJECTS_PRIVILEGE')),
--     ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_MODIFY_PROJECTS_PRIVILEGE')),
--     ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE')),
--     ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_MAKE_ENGINEER_RESPONSE_PRIVILEGE')),
--     ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM privileges WHERE name = 'CAN_MODERATE_TICKET_RESPONSES_PRIVILEGE'));
--
-- INSERT INTO users_roles (user_id, role_id)
-- VALUES
--     ((SELECT id FROM users WHERE email = 'admin@tester.nl'), (SELECT id FROM roles WHERE name = 'ROLE_ADMIN')),
--     ((SELECT id FROM users WHERE email = 'engineer@tester.nl'), (SELECT id FROM roles WHERE name = 'ROLE_ENGINEER')),
--     ((SELECT id FROM users WHERE email = 'user@tester.nl'), (SELECT id FROM roles WHERE name = 'ROLE_USER'));
