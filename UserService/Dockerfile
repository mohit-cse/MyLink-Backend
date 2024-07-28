FROM openjdk:17
EXPOSE 8081
WORKDIR ./UserService
ADD target/user_service.jar user_service.jar
ENTRYPOINT ["java", "-jar", "/user_service.jar"]