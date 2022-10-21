FROM maven:3.8-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
COPY mvnw /home/app
COPY ./.mvn /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:17-jdk
WORKDIR /opt/app
COPY --from=build /home/app/target/taskfly-0.0.1-SNAPSHOT.jar /usr/local/lib/taskfly.jar
EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/usr/local/lib/taskfly.jar" ]
