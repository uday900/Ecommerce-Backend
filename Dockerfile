FROM maven:3.8.4-openjdk-17 AS build


WORKDIR /app


COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/Ecommerce-backend-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/app/Ecommerce-backend-0.0.1-SNAPSHOT.jar" ]
