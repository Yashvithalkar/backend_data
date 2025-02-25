# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and source code
COPY . /app

# Give execute permission to the Maven wrapper
RUN chmod +x mvnw

# Install Maven dependencies and package the application
RUN ./mvnw clean package -DskipTests

# Expose the application's port
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "target/*.jar"]
