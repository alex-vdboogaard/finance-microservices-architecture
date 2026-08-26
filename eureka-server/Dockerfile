# syntax=docker/dockerfile:1

# Build stage
FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /workspace

# 1) Build and install the shared 'common' library into the local Maven repo
COPY pom.xml pom.xml
RUN mvn -B -N -f pom.xml install
COPY common/pom.xml common/pom.xml
COPY common/src common/src
RUN mvn -B -DskipTests -f common/pom.xml clean install

# 2) Build eureka-server
COPY eureka-server/pom.xml app/pom.xml
COPY eureka-server/src app/src
RUN mvn -B -DskipTests -f app/pom.xml clean package

# Run stage
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy jar from build stage
COPY --from=build /workspace/app/target/eureka-server-0.0.1-SNAPSHOT.jar eureka-server.jar

# Expose port
EXPOSE 8761

# Run the app
ENTRYPOINT ["java","-jar","eureka-server.jar"]
