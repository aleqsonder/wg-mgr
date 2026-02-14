FROM openapitools/openapi-generator-cli AS gen
WORKDIR /gen
COPY openapi.yml .
RUN openapi-generator-cli generate -i openapi.yml -g spring -o /gen/server

FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY --from=gen /gen/server/pom.xml .
RUN mvn -B dependency:go-offline
COPY --from=gen /gen/server/src src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
