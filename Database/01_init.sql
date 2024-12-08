-- Postgre SQL

--user table
--User's caught fishes
--admin table

CREATE SCHEMA api_schema;

CREATE TABLE api_schema.user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE api_schema.fish (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    weight DECIMAL(10,2) NOT NULL,
    length DECIMAL(10,2) NOT NULL,
    location VARCHAR(80) NOT NULL,
    user_id INT REFERENCES api_schema.user(id)
);

CREATE TABLE api_schema.admin (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create a new user and grant privileges
DO
$$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'api_user') THEN
        CREATE USER api_user WITH ENCRYPTED PASSWORD 'api_user_password';
    END IF;
END
$$;

GRANT ALL PRIVILEGES ON DATABASE api_database TO api_user;
GRANT USAGE ON SCHEMA api_schema TO api_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA api_schema TO api_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA api_schema TO api_user;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA api_schema TO api_user;