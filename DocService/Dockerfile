FROM openjdk:17
EXPOSE 8082
WORKDIR ./DocService
ADD target/doc_service.jar doc_service.jar
ENTRYPOINT ["java", "-jar", "/doc_service.jar"]