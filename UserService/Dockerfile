FROM arm64v8/openjdk:17-ea
EXPOSE 8081
WORKDIR ./UserService
ADD target/user_service.jar user_service.jar
ENTRYPOINT ["java", "-jar", "/user_service.jar"]