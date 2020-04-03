FROM openjdk:8-jdk-alpine
LABEL maintainer="jonathan.smith@ww-informatik.de"
ARG JAR_FILE=target/*.war
COPY ${JAR_FILE} backend.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","/backend.war"]