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
    (nextval('service_tickets_seq'), 'Issue with Server', 0, 0, 'Experiencing connectivity problems', 1, 0, '2023-12-28T09:00:00'),
    (nextval('service_tickets_seq'), 'Network Latency', 1, 1, 'Experiencing slow internet speeds', 51, 0, '2024-01-02T10:30:00'),
    (nextval('service_tickets_seq'), 'Software Installation', 1, 1, 'Request for software setup on workstation', 101, 0, '2024-01-02T11:45:00'),
    (nextval('service_tickets_seq'), 'Email Configuration Issue', 2, 0, 'Unable to send/receive emails', 151, 0, '2024-01-02T13:15:00');

/* SERVICE TICKET RESPONSE DATA */
INSERT INTO ticket_responses (id, response_type, response, creation_date, ticket_id)
VALUES
    (nextval('ticket_responses_seq'), 'basic_response', 'Tried rebooting the router', '2023-12-28T09:10:00', 1),
    (nextval('ticket_responses_seq'), 'basic_response', 'Performed speed test, investigating the issue', '2024-01-02T10:35:00', 51),
    (nextval('ticket_responses_seq'), 'basic_response', 'Identified high latency due to ISP issue, contacting ISP support', '2024-01-02T11:00:00', 51),
    (nextval('ticket_responses_seq'), 'basic_response', 'ISP confirmed issue in the area, estimated resolution time provided', '2024-01-02T11:30:00', 51),
    (nextval('ticket_responses_seq'), 'basic_response', 'Contacting user to gather software requirements', '2024-01-02T11:50:00', 101),
    (nextval('ticket_responses_seq'), 'basic_response', 'Received software specifications, initiating installation', '2024-01-02T12:15:00', 101),
    (nextval('ticket_responses_seq'), 'basic_response', 'Software installation completed successfully', '2024-01-02T13:00:00', 101),
    (nextval('ticket_responses_seq'), 'basic_response', 'Checking mail server settings for possible issues', '2024-01-02T13:20:00', 151),
    (nextval('ticket_responses_seq'), 'basic_response', 'Adjusted SMTP settings, testing outgoing mail', '2024-01-02T13:40:00', 151),
    (nextval('ticket_responses_seq'), 'basic_response', 'Incoming mail settings updated, testing incoming mail', '2024-01-02T14:00:00', 151);
