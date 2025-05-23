FROM eclipse-temurin:21-jdk as builder

WORKDIR /app
COPY . .

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=builder /app/target/courier-tracking-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8100

ENTRYPOINT ["java", "-jar", "app.jar"]