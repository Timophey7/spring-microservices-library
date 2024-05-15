#FROM maven:3.8.5-openjdk-17 AS build
#COPY /src /src
#COPY pom.xml /
#RUN mvn -f /pom.xml clean package

FROM openjdk:17

COPY /target/orders-service-0.0.1-SNAPSHOT.jar order-service.jar

ENTRYPOINT ["java", "-jar", "order-service.jar"]