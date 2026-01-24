FROM maven:3.9-amazoncorretto-25 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY . .
RUN mvn clean package -DskipTests


FROM amazoncorretto:25-alpine

WORKDIR /app

COPY --from=build app/target/reservation-system.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]