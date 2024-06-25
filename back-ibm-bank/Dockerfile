FROM ubuntu:latest AS build
LABEL authors="marouane"

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y

WORKDIR /app

COPY . .

RUN apt-get install maven -y
RUN mvn clean install
RUN ls -la /app/target/

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /app/target/ibm-bank-challenge-1.0.0.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]