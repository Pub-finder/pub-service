# Build the .jar file
FROM gradle:7.6.0-jdk17 AS build
COPY . .
RUN gradle clean bootJar --no-daemon

# Run the app
FROM openjdk:17-jdk-alpine
COPY --from=build /build/libs/pub-service-1.jar app.jar

#ARG JAR_FILE=build/libs/pub-service-1.jar
#COPY ${JAR_FILE} app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
