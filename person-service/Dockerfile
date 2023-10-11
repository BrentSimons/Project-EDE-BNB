FROM openjdk:17-jdk-alpine
EXPOSE 8095
ADD target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]