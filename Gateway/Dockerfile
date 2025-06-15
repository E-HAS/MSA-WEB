FROM openjdk:17
VOLUME /tmp
COPY build/libs/centerGateway.jar Gateway-server.jar
ENTRYPOINT ["java","-jar","Gateway-server.jar"]
#docker build -t gateway-server .