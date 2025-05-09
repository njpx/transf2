# Stage 1: Build WAR
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# This layer caching optimization will save time on rebuilds
RUN mvn dependency:go-offline

COPY src ./src
COPY mvnw ./mvnw
COPY .mvn ./.mvn
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Run WAR
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.war app.war
EXPOSE 8080

# Adding environment variable configuration support
ENTRYPOINT ["java", "-jar", "app.war"]