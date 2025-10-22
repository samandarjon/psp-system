FROM eclipse-temurin:21-jre

WORKDIR /app

COPY build/libs/*.jar app.jar
# Expose Spring Boot default port
EXPOSE 8080

# Start the app
ENTRYPOINT ["java","-jar","/app/app.jar"]
