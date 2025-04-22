FROM openjdk:17
VOLUME /tmp
COPY build/libs/config.jar config-server.jar
ENTRYPOINT ["java","-jar","config-server.jar"]