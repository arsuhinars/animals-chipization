
# Build stage
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /backend

COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline

COPY ./src ./src
COPY docker.application.properties ./src/main/java/resources/application.properties
RUN ./mvnw clean install


# Run stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /backend

COPY --from=build /backend/target/*.jar /backend/app.jar

ENTRYPOINT [ "java", "-jar", "/backend/app.jar" ]
