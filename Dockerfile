# Use a JDK image as the base
FROM maven:3.8.5-openjdk-18 AS build

# Copy the project files (including pom.xml) into the container
COPY . .

# Build the project using Maven
RUN mvn clean package -DskipTests

# Use a JRE image as the base
FROM openjdk:18-jdk-slim

# Copy the built JAR file into the container
COPY --from=build target/Elevation-1.0-SNAPSHOT.jar target/elevation-bot.jar

# Specify the command to run the application
CMD ["java", "-jar", "target/Elevation-1.0-SNAPSHOT.jar"]