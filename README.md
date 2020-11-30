# kafka-consumer-demo

Demo backend application for consuming heartbeat data and for providing a RESTful API to serve heartbeat spikes. The application, built with SpringBoot, uses Kafka Consumer API to consume messages and stores them in MongoDB. 

## Prerequisites

Have a look at required dependencies to build project:

* JDK 11
* Maven 3.6.1 or newer
* Docker 19.03.13 or newer

## How to Build

Run `mvn clean install` to build and install.

## How to Run

Easiest way to run the application is to use docker-compose.yml in the root directory of the project.

Run `sudo docker-compose up`

TODO...

## Unit Tests & Coverage

After building the project, run `mvn test && mvn jacoco:report` to execute unit tests and to generate coverage report which is accessible at _target/site/jacoco/index.html_

## RESTful API

The application provides a RESTful API that can be consumed by a frontend application. Its documentation is auto-generated and can be accessed by `http://localhost:8080/api/swagger-ui/`.

### Usage

A sample usage is provided below. Just replace curly braces with actual values and run the command:

```
curl -X GET "http://localhost:8080/api/{roomId}/heartbeat?threshold={threshold}&durationBegin={durationBegin}&durationEnd={durationEnd}" -H "accept: */*"
```

You can also test the application by using the RESTful API documentation page.

## Configuration

The backend application can be configured in the file [application.yml](./kafka-consumer/src/main/resources/application.yml).

## Coding Style

[Google coding style](./kafka-consumer/google-coding-style.xml) is used to format code. Additionally, [Udacity Git Commit Style](https://udacity.github.io/git-styleguide/) is used.

## TODO

* Make use of partitions to load-balance (some field such as roomId might be used as a key provider)
* docker-compose configuration with Kafka clusters
* Integration tests
