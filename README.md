# Integration tests with Spring, Amazon DynamoDB and TestContainers

Deploying cloud native Spring applications on cloud providers allows you to leverage great managed tools such as databases, object storage systems and queues. In my current project many of our microservices, which are deployed on Amazon ECS, use DynamoDB for storage. This is great of course, but how do you integration test these services? In this post I’ll show you how you can use TestContainers to do integration tests in a Spring microservice backed by DynamoDB.

[Full blog post here](https://niels.nu/blog/2018/spring-dynamodb-integration-testing.html)