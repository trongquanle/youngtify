FROM openjdk:8-alpine
VOLUME /tmp
ADD target/youngtify-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT exec java -jar app.jar