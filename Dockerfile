FROM eclipse-temurin:11-jdk as build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apt-get update && apt-get install -y maven
RUN mvn package -DskipTests

FROM eclipse-temurin:11-jre
WORKDIR /app
COPY --from=build /app/target/invoices-backend-0.0.1-SNAPSHOT.jar app.jar
RUN apt-get update && apt-get install -y curl
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
