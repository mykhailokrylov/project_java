-- Postgre SQL

--user table
--User's caught fishes
--admin table

CREATE TABLE user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE fish (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    weight DECIMAL(10,2) NOT NULL,
    length DECIMAL(10,2) NOT NULL,
    location VARCHAR(80) NOT NULL,
    user_id INT REFERENCES user(id)
);

CREATE TABLE admin (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create a new user and grant privileges
CREATE USER api_user WITH ENCRYPTED PASSWORD 'api_user_password';
GRANT ALL PRIVILEGES ON DATABASE api_database TO api_user;