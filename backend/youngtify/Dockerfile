FROM maven:3.6-jdk-8-alpine

COPY . /app

WORKDIR /app

RUN mvn clean package

ENTRYPOINT exec java -jar /app/target/youngtify-0.0.1-SNAPSHOT.jar app.jar