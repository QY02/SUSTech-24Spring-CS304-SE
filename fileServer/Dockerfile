FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY ./target/fileServer-0.0.1-SNAPSHOT.jar ./fileServer-0.0.1-SNAPSHOT.jar

EXPOSE 25572

ENTRYPOINT ["java", "-jar", "fileServer-0.0.1-SNAPSHOT.jar"]