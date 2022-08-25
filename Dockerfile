FROM openjdk:17-jdk-alpine
EXPOSE 8082
COPY target/auth.jar auth.jar
ENTRYPOINT ["java", "-jar","auth.jar"]