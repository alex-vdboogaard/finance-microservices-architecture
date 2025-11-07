# syntax=docker/dockerfile:1

# =============================
# 1. Build stage
# =============================
FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /app

# Use a stable Central mirror to avoid repo.maven.apache.org DNS issues
# Place it outside /root/.m2 so cache mounts don't mask it
COPY maven-settings.xml /app/maven-settings.xml

# Copy sources
COPY pom.xml .
COPY src ./src

# Build with cached local repo and custom settings
# Mount only the repository dir to avoid masking the settings file
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn -B -s /app/maven-settings.xml clean package -DskipTests

# =============================
# 2. Run stage
# =============================
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/api-gateway-0.0.1-SNAPSHOT.jar api-gateway.jar

# Expose port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java","-jar","api-gateway.jar"]
