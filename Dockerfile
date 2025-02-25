# Use an official OpenJDK 21 image as the base image
FROM eclipse-temurin:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy Maven wrapper and project files
COPY . /app

# Give execute permissions to Maven wrapper
RUN chmod +x mvnw

# Install dependencies and package the application
RUN ./mvnw clean package -DskipTests

# Copy the built JAR file into the final image
RUN cp target/*.jar app.jar

# Expose the application's port
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
