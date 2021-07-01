
# Product Catalog Service

REST Api which stores a Product Catalog and propagates data to 3rd party downstream services.


# Introduction

In this README file you'll find enough information to Install, Run and test this application.
You will also find the designs considerations there were taken, and provide information to how this was implemented, it's limitations, and how this project performance could be improved.

## Prerequisites

* Java JDK 1.8 : https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
* Maven 3 : https://maven.apache.org/install.html

## Compile and Run

 In the project folder run this command to compile and start the application, this also starts an H2 database in memory and run the migrations
 > mvn clean spring-boot:run

## API Documentation (Swagger)

 Running application you can access a build-in documentation
- [Swagger](http://localhost:8080/swagger-ui.html)


## Project Decisions

- The project was built under the REST architectural style using SpringFramework, making use of AWS Services to increase it's capabilities and provide a fault tolerant solution.
- Uses AWS SNS to publish an event and propagate this information to anyone subscribed to the topic.
- Uses AWS SQS as a queue system, which is subscribed to the SNS Topic, allowing the application to poll messages in batches, and process them asynchronously. SQS will also allow the application to retry a desired amount of times the processing of a message. If a Message is imposible to process and the amount of retries is exceeded, to message is dequeued and sent into a Dead-Letter-Queue, which allows the application to re-process the message with a different retry strategy.
- The application stores it's registered products in AWS DynamoDB, a NoSQL Database.

![image](https://user-images.githubusercontent.com/22630277/124056461-26bf7680-d9fc-11eb-9cc6-3163401f569b.png)


## Limitations & Improvements

- A cache layer could result beneficial to reduce processing time. If the stored product resource has already been accessed, and it's data has not changed, then returning a cached result from the previous process would reduce time consumption.
- The service should be decouple into another service component that handles the data propagation to 3rd parties API's.
- The consumer could become code hard to maintain as it is and doesn't scale. It would be wise to create a consumer for each 3rd Parties Integrations, spreading the load into different queues, with it's own strategy and code maintenance, specially for those whose API's methods are deprecated in short periods of time (i.e Facebook Business API)
- There's no security layer implement to authenticate and authorize API requests to prevent non desired changes to the product catalog.
- An Audit layer would prove useful in order to preserve data integrity and detect who changed a resource.
- The Dead-Letter-Queue could be use to retry in a exponential way or send an alert notification in order to take action into it.
