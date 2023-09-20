# Policeman Service

Read this in other languages: [English](README.md), [Русский](README.ru.md)

This service is designed to obtain a random or sequential vehicle registration number in the format `A000AA 116 RUS`, 
where A can be any character from the list `[A, E, T, O, P, H, Y, K, X, C, B, M]`, 1 can be any digit, and `116 RUS` is a constant.

MongoDB was chosen as the database system because it typically provides high performance for insertion and retrieval operations.

## Local launch of the service

1) Before running the service, you need to build the JAR file first using the following command:

```bash
mvn package
```

2) The service utilizes Docker, so to launch it, simply execute the command below.

```bash
docker-compose up --build -d
```

Two containers will be raised (application and MongoDB database) as described in the docker-compose.yml file located at the project root. 
To confirm that the containers have been successfully brought up, you can use the command below and check the status (Up).

```bash
docker ps
```

3) You can stop the service using the following command:

```bash
docker-compose stop
```

## Main functionality:
1) Obtaining a random number
    * url: /api/v1/numbers/random GET
    * incoming value: The authentication token must be provided in the request header
    * return value: A string with the content type of text/plain (for example, `A000AA 116 RUS`)
2) Obtaining the next number
    * url: /api/v1/numbers/next GET
    * incoming value: The authentication token must be provided in the request header
    * return value: A string with the content type of text/plain (for example, `A000AA 116 RUS`)

More detailed description can be found in the Swagger documentation, where you can also test the requests. Swagger URL: http://localhost:8080/swagger-ui/index.html.

Additionally, in the requests.http file located at the project root, examples of requests have been provided, which you can execute within a development environment of your choice or using any other method.

### List of libraries and technologies used for building the service
   * [spring boot](https://spring.io/projects/spring-boot) as the primary web framework
   * [jjwt](https://github.com/jwtk/jjwt)  for JWT authentication
   * [modelmapper](https://modelmapper.org) for entity mapping
   * [swagger-v3](https://swagger.io/specification/) Swagger for API documentation
   * [testcontainers](https://testcontainers.com) library providing the ability to run test containers for integration testing
   * СУБД MongoDb
   * Docker, docker-compose

## Running tests


Integration tests have been written for [CarNumberController.java](#src/main/java/ru/inovus/policeman/controller/CarNumberController.java), which can be found in the `src/test` folder.

```bash
mvn test
```

Tests can also be run through the development environment interface, for example Intellij Idea.

## Build Framework

* [Maven](https://maven.apache.org)
