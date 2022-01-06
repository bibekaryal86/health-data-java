FROM openjdk:11-jre-slim-bullseye
RUN adduser --system --group springdocker
USER springdocker:springdocker
ARG JAR_FILE=app/build/libs/health-data-java.jar
COPY ${JAR_FILE} health-data-java.jar
ENTRYPOINT ["java","-jar", \
#"-DPORT=6001", \
#"-DTZ=America/Denver", \
#"-DDB2USR=some_username", \
#"-DDB2PWD=some_password", \
#"-DDB2HST=some_host", \
#"-DDB2DEP=some_port", \
#"-DBASIC_AUTH_USR=another_username", \
#"-DBASIC_AUTH_PWD=another_password", \
"/health-data-java.jar"]
# ENV variables provided in docker-compose
