FROM openjdk:11
ADD ./target/kafka-consumer-0.0.1-SNAPSHOT.jar /usr/src/kafka-consumer-0.0.1-SNAPSHOT.jar
WORKDIR usr/src
ENTRYPOINT ["java","-jar", "kafka-consumer-0.0.1-SNAPSHOT.jar"]