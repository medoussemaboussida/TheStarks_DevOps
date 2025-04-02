FROM openjdk:17-jdk

# Set environment variables
ENV NEXUS_URL="http://http://192.168.50.4:8081/repository/maven-releases/tn/esprit/kaddem_Roumaissa/0.0.1/kaddem_Roumaissa-0.0.1.jar"
ENV JAR_FILE="kaddem_Roumaissa-0.0.1.jar"
ENV NEXUS_USERNAME="admin"
ENV NEXUS_PASSWORD="admin"

RUN curl -u $NEXUS_USERNAME:$NEXUS_PASSWORD -o $JAR_FILE $NEXUS_URL
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "kaddem_Roumaissa-0.0.1.jar"]