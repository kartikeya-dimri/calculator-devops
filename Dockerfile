FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN apt-get update \
    && apt-get install -y --no-install-recommends maven \
    && mvn clean package \
    && apt-get purge -y maven \
    && apt-get autoremove -y \
    && rm -rf /var/lib/apt/lists/*

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/calculator-devops-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
