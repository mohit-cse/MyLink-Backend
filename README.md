# MyLink-Backend
This repository contains microservices for **MyLink** app. Services are developed using Spring Boot with Java. 
#### Key points about *MyLink-Backend*
* All services are deployed in a self-hosted **Kubernetes** cluster.
* It has Github workflows for each service for building and pushing images to **dockerhub**.

## Services
### User Service
This service is responsible for accounts and auth related endpoints.

#### Key points about *User Service*
* It uses **MariaDB (MySQL)** for storing user data.
* It issues **JWT** tokens and provides validation endpoint. JWT token is for session.

#### User service has following endpoints
* `POST` /api/auth/signup
> This endpoint is for signup.
* `POST` /api/auth/login
> This endpoint is for login.
* `POST` /api/auth/authenticateToken
> This endpoint is for authenticating the JWT token and retrieving userID.
* `DELETE` /api/auth/deleteAccount
> This endpoint is for deleting the account.

### Doc Service
This service is responsible for document and storage related endpoints. It can store and retrieve any type of file. All endpoints are secured by JWT token issued by the user service.

#### Key points about *Doc Service*
* It uses **Elasticsearch** for storing metadata and **MinIO** bucket storage for storing documents.
* Each request gets validated with JWT token issued by **User Service**. Validation is done by **User Service** and API calls are made using **Retrofit** library.

#### Doc service has following endpoints
* `POST` /api/doc/upload
> This endpoint is for uploading the document.
* `GET` /api/doc/allDocs
> This endpoint is for getting metadata for all docs for a user.
* `GET` /api/doc/{docId}
> This endpoint is for getting the a particular documented by id for a user.
* `GET` /api/doc/getDocByName/{docName}
> This endpoint is for getting a particular documented by name for a user.
* `DELETE` /api/doc/{docId}
> This endpoint is for deleting a particular documented by id for a user.
* `DELETE` /api/doc/deleteDocByName/{docName}
> This endpoint is for deleting a particular documented by name for a user.

### API Gateway
This is the entrypoint to all other services. It offers a Swagger UI as well for directly interacting with the APIs of underlying services. 

#### Key points about *API Gateway*
* Uses **Reactive Spring Cloud Gateway** 
* Provides integrated **Swagger UI** for all underlying services. 
* Blocks all unauthorized API calls. 
