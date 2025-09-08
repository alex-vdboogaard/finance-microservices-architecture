# Build stage
FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven files and build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/eureka-server-0.0.1-SNAPSHOT.jar eureka-server.jar

# Expose port
EXPOSE 8761

# Run the app
ENTRYPOINT ["java","-jar","eureka-server.jar"]
