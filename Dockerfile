FROM openjdk:11-jre-slim-bullseye
RUN adduser --system --group springdocker
USER springdocker:springdocker
ARG JAR_FILE=app/build/libs/health-data-java.jar
COPY ${JAR_FILE} health-data-java.jar
ENTRYPOINT ["java","-jar", \
"/health-data-java.jar"]
# ENV variables to add in docker-compose.yml
