FROM openjdk:17

COPY ./target/*.jar /app/bus-rapid-transit.jar
WORKDIR /app
CMD ["java", "-jar", "bus-rapid-transit.jar"]