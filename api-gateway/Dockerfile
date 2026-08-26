# syntax=docker/dockerfile:1

# =============================
# 1. Build stage
# =============================
FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /workspace

# 1) Build and install the shared 'common' library into the local Maven repo
COPY pom.xml pom.xml
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn -B -N -f pom.xml install
COPY common/pom.xml common/pom.xml
COPY common/src common/src
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn -B -DskipTests -f common/pom.xml clean install

# 2) Build api-gateway
COPY api-gateway/pom.xml app/pom.xml
COPY api-gateway/src app/src
COPY api-gateway/maven-settings.xml app/maven-settings.xml

RUN --mount=type=cache,target=/root/.m2/repository \
    mvn -B -s app/maven-settings.xml -f app/pom.xml clean package -DskipTests

# =============================
# 2. Run stage
# =============================
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy jar from build stage
COPY --from=build /workspace/app/target/api-gateway-0.0.1-SNAPSHOT.jar api-gateway.jar

# Expose port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java","-jar","api-gateway.jar"]
