FROM openjdk:17
EXPOSE 8083
WORKDIR ./ApiGateway
ADD target/api_gateway.jar api_gateway.jar
ENTRYPOINT ["java", "-jar", "/api_gateway.jar"]