DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS item_requests CASCADE;
DROP TABLE IF EXISTS booking CASCADE;

CREATE TABLE IF NOT EXISTS users (
  id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS  item_requests (
 id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
 description VARCHAR(255),
 requestor_id INT NOT NULL,
 created timestamp NOT NULL,
 CONSTRAINT pk_item_requests PRIMARY KEY (id),
 CONSTRAINT fk_item_requests_to_users FOREIGN KEY(requestor_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS items (
  id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  owner_id INT NOT NULL,
  description VARCHAR(255),
  available BOOLEAN NOT NULL,
  request_id INT,
  CONSTRAINT pk_items PRIMARY KEY (id),
  CONSTRAINT fk_items_to_users FOREIGN KEY(owner_id) REFERENCES users(id),
  CONSTRAINT fk_items_to_request FOREIGN KEY(request_id) REFERENCES item_requests(id)
);


CREATE TABLE IF NOT EXISTS booking (
  id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  start_time timestamp NOT NULL,
  end_time timestamp NOT NULL,
  item_id INT NOT NULL,
  booker_id INT NOT NULL,
  status VARCHAR(50) NOT NULL,
  CONSTRAINT pk_booking PRIMARY KEY (id),
  CONSTRAINT fk_booking_to_users FOREIGN KEY(booker_id) REFERENCES users(id),
  CONSTRAINT fk_booking_to_items FOREIGN KEY(item_id) REFERENCES items(id)
);