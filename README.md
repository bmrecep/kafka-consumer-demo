# kafka-consumer-demo

Demo backend application for consuming heartbeat data and for providing a RESTful API to serve heartbeat spikes. The application, built with SpringBoot, uses Kafka Consumer API to consume messages and stores them in MongoDB. 

## Prerequisites

Have a look at required dependencies to build application:

* JDK 11
* Maven 3.6.1 or newer
* Docker 19.03.13 or newer

## How to Build

Run `mvn clean install` to build and install.

## How to Run

### Running all with Docker Compose

Easiest way to run the application is to use docker-compose.yml in the root directory of the project.

After building the application, run `sudo docker-compose up`. This will start up the backend application service as well as its dependant services (i.e. Kafka and MongoDB).

## Unit Tests & Coverage

After building the application, run `mvn test && mvn jacoco:report` to execute unit tests and to generate coverage report which is accessible at _target/site/jacoco/index.html_

## RESTful API

The application provides a RESTful API that can be consumed by a frontend application. Its documentation is auto-generated and can be accessed at `http://localhost:8080/api/swagger-ui/`.

### Usage

A sample usage is provided below. Just replace curly braces with actual values and run the command:

```
curl -X GET "http://localhost:8080/api/{roomId}/heartbeat?threshold={threshold}&durationBegin={durationBegin}&durationEnd={durationEnd}" -H "accept: */*"
```

Example command with parameters (durationBegin: 12:54:40, durationEnd: 12:59:40, roomId: d5f6c448-bc27-4e5c-8a55-0153065203b7, threshold: 700):

```
curl -X GET "http://localhost:8080/api/d5f6c448-bc27-4e5c-8a55-0153065203b7/heartbeat?durationBegin=12%3A54%3A40&durationEnd=12%3A59%3A40&threshold=700" -H "accept: */*"
```

You can also test the application by using the RESTful API documentation page which provides a Postman-like UI.

## Configuration

The backend application can be configured in the file [application.yml](./kafka-consumer/src/main/resources/application.yml).

## Coding Style

[Google coding style](./kafka-consumer/google-coding-style.xml) is used to format code. Additionally, [Udacity Git Commit Style](https://udacity.github.io/git-styleguide/) is used for commit messages.

## TODO

* Make use of partitions to load-balance (some field such as roomId might be used as a key provider)
* Complete missing unit tests and add integration tests
* Pagination for GET queries
