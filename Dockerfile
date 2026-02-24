# Build the Java application using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy pom.xml and download dependencies separately for faster subsequent builds
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Run the application in a clean, lightweight container
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the generated .jar file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Create the directory for storing uploaded painting images
RUN mkdir uploads

# Expose the port the application runs on
EXPOSE 8081

# Command to start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]