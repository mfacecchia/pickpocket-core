# Build step
FROM maven:3.9.16-eclipse-temurin-21-noble AS build

WORKDIR /app

COPY src ./src
COPY pom.xml .

RUN mvn clean package

# Run step
FROM eclipse-temurin:21-noble AS run

WORKDIR /app

COPY --from=BUILD /app/target/splitnings-1.0-SNAPSHOT.war ./splitnings.war

ENTRYPOINT [ "java", "-jar", "/app/splitnings.war" ]

