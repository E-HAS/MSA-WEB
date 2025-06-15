FROM openjdk:17
VOLUME /tmp
COPY build/libs/center.jar center-server.jar
ENTRYPOINT ["java","-jar","center-server.jar"]
#docker build -t center-server .