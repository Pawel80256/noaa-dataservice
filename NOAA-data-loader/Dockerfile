FROM maven:3.9.6-amazoncorretto-21 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:21-jdk-slim
COPY --from=build /home/app/target/NOAA-data-loader-0.0.1-SNAPSHOT.jar /usr/local/lib/NOAA-data-loader.jar
EXPOSE 8080 5005
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005","-jar","/usr/local/lib/NOAA-data-loader.jar"]
