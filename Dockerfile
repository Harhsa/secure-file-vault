# Use a lightweight OpenJDK base
FROM eclipse-temurin:17-jdk-alpine

# Create app directory
WORKDIR /app

# Copy the fat JAR you build into the image
COPY build/libs/securevault-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring app listens on
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java","-jar","app.jar"]
