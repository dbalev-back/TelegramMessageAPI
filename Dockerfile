# Build
FROM gradle:8.5.0-jdk17-alpine AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test --no-daemon

# Run
FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
COPY --from=build /app/${JAR_FILE} app.jar

EXPOSE 7878
ENTRYPOINT ["java", "-jar", "/app.jar"]