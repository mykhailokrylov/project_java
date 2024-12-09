FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/Non-workingApiproject-1.0-SNAPSHOT.jar /app/API.jar
COPY .env /app/.env

CMD ["java", "-jar", "API.jar"]