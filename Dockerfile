# Use a JDK image as the base
FROM openjdk:18-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the project files (including pom.xml) into the container
COPY . .

# Build the project using Maven
RUN mvn -B package

# Specify the command to run the application
CMD ["java", "-jar", "target/Elevation-1.0-SNAPSHOT.jar"]