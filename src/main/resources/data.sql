DROP TABLE IF EXISTS user_directory;

CREATE TABLE user_directory (
  id VARCHAR(250) NOT NULL PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  contact_number VARCHAR(250)
);

INSERT INTO user_directory (id, first_name, last_name) VALUES
  ('S8823678E', 'Kylie', 'Humphrey'),
  ('S7993823H', 'Julianne', 'Henderson'),
  ('S1502746J', 'Samson', 'Villegas'),
  ('S1788749A', 'Frances', 'Dillon'),
  ('S5773720D', 'Julie', 'Magnate'),
  ('G2313040N', 'Joselyn', 'Henderson'),
  ('G6363972P', 'Natalee', 'Magnate'),
  ('G8333471T', 'Jefferson', 'Magnate'),
  ('T1917402B', 'Camren', 'Humphrey'),
  ('F5399943M', 'Alessandro', 'Magnate');



DROP TABLE IF EXISTS address_directory;

CREATE TABLE address_directory (
  user_id VARCHAR(250) NOT NULL,
  address_line1 VARCHAR(250),
  address_line2 VARCHAR(250),
  city VARCHAR(250),
  country VARCHAR(250) NOT NULL,
  postal_code VARCHAR(250) NOT NULL
);

INSERT INTO address_directory (user_id, country, postal_code) VALUES
  ('S8823678E', 'Singapore', '520237'),
  ('S8823678E', 'Singapore', '520241'),
  ('F5399943M', 'Singapore', '520247'),
  ('T1917402B', 'Singapore', '520248'),
  ('G8333471T', 'Singapore', '520249'),
  ('G6363972P', 'Singapore', '520253'),
  ('S1502746J', 'Malaysia', '520241'),
  ('S7993823H', 'Malaysia', '123456');