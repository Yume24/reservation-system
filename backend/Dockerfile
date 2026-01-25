FROM maven:3.9-amazoncorretto-25-alpine AS build

WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY . .
RUN mvn clean package -DskipTests


FROM amazoncorretto:25-alpine

WORKDIR /app

RUN addgroup -S spring \
    && adduser -S springuser -G spring \
    && chown springuser:spring /app

COPY --from=build build/target/reservation-system.jar app.jar
RUN chown springuser:spring app.jar


USER springuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]