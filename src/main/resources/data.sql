-- noinspection SqlDialectInspectionForFile

/* USER DATA
INSERT INTO users(username, password, enabled)VALUES('user', 'password', true) */

/* PROJECTS DATA */
INSERT INTO projects(id, name, city, zip_code, street, house_number)VALUES(nextval('projects_seq'), 'Rabobank Utrecht', 'Utrecht', '1013 GM', 'Croeselaan', 18)