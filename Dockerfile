# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM openjdk:17-jdk-slim
WORKDIR /app
RUN apt-get update && apt-get install -y wait-for-it
COPY --from=build /app/target/web-library-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 228
CMD ["wait-for-it", "db:3306", "--", "java", "-jar", "app.jar"]