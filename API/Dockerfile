FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/Non-workingApiproject.jar /app/Non-workingApiproject.jar
COPY .env /app/.env

CMD ["java", "-jar", "Non-workingApiproject.jar"]
