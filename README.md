
# Drone fleet

## Build

System requirements:

* jdk 17
* docker
* docker-compose

```bash
./mvnw clean install
```

  

## How to run

Run following command:

```bash
docker-compose up 
```

And then go to [Swagger UI](http://localhost:8088/swagger-ui.html).
The service is come up with prepopulated data. 


## How to stop

```bash
docker-compose down
```

## Getting started

[Application configuration](https://github.com/dpimkin/drone-fleet/blob/main/src/main/resources/application-dev.yaml)

[Database schema](https://github.com/dpimkin/drone-fleet/blob/main/src/main/resources/db/migration/V1_0__Initial.sql)

[Database populate script](https://github.com/dpimkin/drone-fleet/blob/main/src/main/resources/db/migration/V1_1__populate.sql)

[Swagger UI](http://localhost:8088/swagger-ui.html)


## Software architecture design

I anticipate that network packet loss may occur due to the use of wireless networking in developing countries. As a result, it is important for the clients of this service to rely heavily on retries in order to improve network handover reliability. To support this retry policy, the service must have idempotent APIs, which I have implemented by adding an [idempotency key](https://multithreaded.stitchfix.com/blog/2017/06/26/patterns-of-soa-idempotency-key/) and caching.

This is a greenfield project, so it is important to ensure that it is easy to support and maintain. That's why I prefer to use conventions over configuration patterns in the design. Additionally, a docker-compose file has been provided to facilitate the creation of a simplified development sandbox environment that can be run on a laptop. The database migration process is handled using Flyway scripts.

All functional and non-functional requirements and business scenarios have been thoroughly tested, which significantly reduces the turnaround time for resolving issues.

Proper documentation is crucial for the success of any service. To facilitate a quick start, the service includes an [OpenAPI/Swagger UI](http://localhost:8088/swagger-ui.html) for easy access to documentation.

Although it is not yet determined whether the service infrastructure will be deployed in the cloud, I have designed it to be compatible with a Kubernetes environment and made the configuration externalizable for flexibility. The service has also been designed to support health checks.

In order to optimize performance and scalability, I have chosen not to store image blobs directly in the database. Instead, I have opted to use an object store and provide links to access the images as needed.


 





