FROM maven:3.8-openjdk-16 AS MAVEN_BUILD

MAINTAINER Felipe Flores (felipeguilhermefs@gmail.com)

#We copy source and pom to build without the need of a local java or maven
COPY pom.xml /build/
COPY src /build/src/

#Build is done in this container stage
#All dependencies will be downloaded which might take a while
#We could use local dependencies with volumes, but that would require a local maven
WORKDIR /build/
RUN mvn clean package

FROM openjdk:16-alpine

WORKDIR /app

#To reduce image size we just a JRE and .jar file
COPY --from=MAVEN_BUILD /build/target/diff-1.0.0.jar /app/

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "diff-1.0.0.jar"]
