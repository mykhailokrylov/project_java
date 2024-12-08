FROM postgres:latest

ENV POSTGRES_DB=api_database
ENV POSTGRES_USER=api_user
ENV POSTGRES_PASSWORD=api_user_password

COPY 01_init.sql /docker-entrypoint-initdb.d/
