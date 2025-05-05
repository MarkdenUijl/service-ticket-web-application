# Import JDK runtime
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy built JAR into container
COPY target/service-tickets.jar app.jar

# Expose port 8443 for HTTPS traffic
EXPOSE 8443

# Run the Springboot application
CMD ["java", "-jar", "app.jar"]