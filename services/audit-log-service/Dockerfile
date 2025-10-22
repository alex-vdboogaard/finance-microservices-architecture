# syntax=docker/dockerfile:1

# Build stage
FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /workspace

# 1) Build and install the shared 'common' library into the local Maven repo
# Expectation: build context is the repo root so we can copy the common module
COPY pom.xml pom.xml
RUN mvn -B -N -f pom.xml install
COPY common/pom.xml common/pom.xml
COPY common/src common/src
RUN mvn -B -DskipTests -f common/pom.xml clean install

# 2) Build the audit-log-service which depends on 'common'
COPY services/audit-log-service/pom.xml app/pom.xml
COPY services/audit-log-service/src app/src
RUN mvn -B -DskipTests -f app/pom.xml clean package

# Run stage
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy bootable jar from build stage
COPY --from=build /workspace/app/target/audit-log-service-0.0.1-SNAPSHOT.jar audit-log-service.jar

# Expose port
EXPOSE 8001

# Run the app
ENTRYPOINT ["java","-jar","audit-log-service.jar"]
