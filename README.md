# MyLink-Backend
This repository contains microservices for **MyLink** app. Services are developed using Spring Boot with Java.
## Services
### User Service
This service is responsible for accounts and auth related endpoints.
#### User service has following endpoints
* `POST` /api/auth/signup
> This endpoint is for signup.
* `POST` /api/auth/login
> This endpoint is for login.
* `POST` /api/auth/authenticateToken
> This endpoint is for authenticating the jwt token and retrieving userID.
* `DELETE` /api/auth/deleteAccount
> This endpoint is for deleting the account.

### Doc Service
This service is responsible for document and storage related endpoints. It can store and retrieve any type of file.
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