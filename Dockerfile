FROM openjdk:19-alpine
COPY target/Shop-0.0.1-SNAPSHOT.jar /app.jar
CMD ["java","-jar","/app.jar"]