#Step 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /build

COPY . .

# Tests are also run here, if they fail, build fails
RUN mvn clean package

# Step 2: Execute
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]