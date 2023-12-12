FROM openjdk:17
MAINTAINER Venkata Yanamadala
COPY target/TopWordFetcher-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]