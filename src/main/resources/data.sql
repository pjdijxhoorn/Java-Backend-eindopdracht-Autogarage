INSERT INTO cars (id, licenseplate, brand, carstatus) VALUES (100, '33-TTB-3', 'Toyota', 'CHECKED_IN');
INSERT INTO cars (id, licenseplate, brand, carstatus)  VALUES (101, '12-LOL-3', 'Toyota', 'INSPECTING');
INSERT INTO cars (id, licenseplate, brand, carstatus)  VALUES (102, '45-WTF-6', 'Toyota', 'REPAIR');
INSERT INTO cars (id, licenseplate, brand, carstatus)  VALUES (103, '78-OMG-9', 'Toyota', 'WASHING');
INSERT INTO cars (id, licenseplate, brand, carstatus)  VALUES (104, '00-OMA-1', 'Toyota', 'READY');
INSERT INTO cars (id, licenseplate, brand, carstatus)  VALUES (105, '00-AAA-0', 'Toyota','PICKED_UP');
INSERT INTO cars (id, licenseplate, brand, carstatus)  VALUES (106, '00-OPA-0', 'Saab', null);

INSERT INTO carparts (id, carpartname, state, checked) VALUES (100, 'TIRES', '3mm profile', false);
INSERT INTO carparts (id, carpartname, state, checked) VALUES (101, 'BRAKES', 'worn out', false);
INSERT INTO carparts (id, carpartname, state, checked) VALUES (102, 'STEERING_LINING', 'aligned', false);
INSERT INTO carparts (id, carpartname, state, checked) VALUES (103, 'LIGHTS', 'breaklight broken', false);
INSERT INTO carparts (id, carpartname, state, checked) VALUES (104, 'SUSPENSION', 'working fine', false);
INSERT INTO carparts (id, carpartname, state, checked) VALUES (105, 'SCHOCK_ABSORPTION', 'working fine', false);

UPDATE carparts SET car_id = 100 WHERE id = 100;
UPDATE carparts SET car_id = 100 WHERE id = 101;
UPDATE carparts SET car_id = 100 WHERE id = 102;
UPDATE carparts SET car_id = 100 WHERE id = 103;
UPDATE carparts SET car_id = 100 WHERE id = 104;
UPDATE carparts SET car_id = 100 WHERE id = 105;

INSERT INTO users (username, password, email, enabled) VALUES ('user', '$2a$12$OUr7IAhav2a8Wij4P0frUOT.6Esb6f/Agb3LqPevjBRdLM7dveoc2','user@test.nl', TRUE);
INSERT INTO users (username, password, email, enabled) VALUES ('admin', '$2a$12$cspEf2prwC.IZLymxeWtLOB3FPrixpY9p9Dcp9G.JSRNpPaOfXGp2', 'admin@test.nl', TRUE);
INSERT INTO users (username, password, email, enabled) VALUES ('desk', '$2a$12$cspEf2prwC.IZLymxeWtLOB3FPrixpY9p9Dcp9G.JSRNpPaOfXGp2', 'admin@test.nl', TRUE);


INSERT INTO authorities (username, authority) VALUES ('user', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_MECHANIC');
INSERT INTO authorities (username, authority) VALUES ('desk', 'ROLE_DESK');


UPDATE cars SET User_username = 'user' WHERE id = 100;
UPDATE cars SET User_username = 'user' WHERE id = 101;
UPDATE cars SET User_username = 'user' WHERE id = 102;
UPDATE cars SET User_username = 'user' WHERE id = 103;
UPDATE cars SET User_username = 'user' WHERE id = 104;
UPDATE cars SET User_username = 'admin' WHERE id = 105;
UPDATE cars SET User_username = 'admin' WHERE id = 106;

INSERT INTO carservices(id, repair_approved, custumor_response, mechanic_done, totalrepaircost, car_id) VALUES (100, true, true, true, 0.0, 100);

INSERT INTO repairs (id, notes, repair_cost, repair_done, carpart_id, carservice_id) VALUES (100, 'worn out tires not enough profile left', 100.0, false, 100, 100);
INSERT INTO repairs (id, notes, repair_cost, repair_done, carpart_id, carservice_id) VALUES (101, 'worn out brakes', 100.0, false, 101, 100);
INSERT INTO repairs (id, notes, repair_cost, repair_done, carpart_id, carservice_id) VALUES (102, 'steering lining was way off', 100.0, false, 102, 100);
INSERT INTO repairs (id, notes, repair_cost, repair_done, carpart_id, carservice_id) VALUES (103, 'brakelight broken', 100.0, false, 103, 100);
INSERT INTO repairs (id, notes, repair_cost, repair_done, carpart_id, carservice_id) VALUES (104, 'Suspension was like a babycart when you drop it of a cliff', 100.0, false, 104, 100);
--INSERT INTO invoices (id, totalrepaircost, totalcost, payed, repair_date, User_username, car_service_id, car_id) VALUES (1, 70.0, 121.0, false, '2020-12-29', 'admin',100, 100 );


