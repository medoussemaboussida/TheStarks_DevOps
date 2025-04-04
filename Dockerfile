FROM openjdk:17-jdk
EXPOSE 8082

ADD target/kaddem_Roumaissa-0.0.1.jar kaddem_Roumaissa-0.0.1.jar


ENTRYPOINT ["java", "-jar", "kaddem_Roumaissa-0.0.1.jar"]


