FROM amazoncorretto:25-alpine

WORKDIR /app

COPY ./target/reservation-system.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]