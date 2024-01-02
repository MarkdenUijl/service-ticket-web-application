-- noinspection SqlDialectInspectionForFile

/* USER DATA
INSERT INTO users(username, password, enabled)VALUES('user', 'password', true) */

/* SERVICE CONTRACT DATA */
INSERT INTO service_contracts(id, type, contract_time_in_minutes, used_time, start_date, end_date)
VALUES
    (nextval('service_contracts_seq'), 0, 480, 0, '2023-01-01', '2023-12-31'),
    (nextval('service_contracts_seq'), 1, 240, 30, '2023-01-01', '2023-12-31'),
    (nextval('service_contracts_seq'), 2, 480, 15, '2023-01-01', '2023-12-31');

/* PROJECTS DATA */
INSERT INTO projects(id, name, city, zip_code, street, house_number, service_contract_id)
VALUES
    (nextval('projects_seq'), 'Amsterdam Tower', 'Amsterdam', '1071 BV', 'Stadhouderskade', 123, 1),
    (nextval('projects_seq'), 'Rotterdam Plaza', 'Rotterdam', '3012 CL', 'Coolsingel', 45, null),
    (nextval('projects_seq'), 'Utrecht Heights', 'Utrecht', '3582 TZ', 'Maliebaan', 67, 51),
    (nextval('projects_seq'), 'The Hague Residency', 'Den Haag', '2597 AK', 'Scheveningseweg', 89, null),
    (nextval('projects_seq'), 'Groningen Central', 'Groningen', '9711 AA', 'Grote Markt', 10, 101),
    (nextval('projects_seq'), 'Eindhoven Tower', 'Eindhoven', '5611 BB', 'Stratumseind', 34, null);

/* SERVICE TICKETS DATA */
INSERT INTO service_tickets (id, name, status, type, description, project_id, minutes_spent, creation_date)
VALUES
    (nextval('service_tickets_seq'), 'Issue with Server', 0, 0, 'Experiencing connectivity problems', 1, 0, '2023-12-28T09:00:00');

/* SERVICE TICKET RESPONSE DATA */
INSERT INTO ticket_responses (id, response_type, response, creation_date, ticket_id)
VALUES
    (nextval('ticket_responses_seq'), 'basic_response', 'Tried rebooting the router', '2023-12-28T09:10:00', 1)