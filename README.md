
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
docker-compose up -d 
```

And then go to [Swagger UI](http://localhost:8088/swagger-ui.html)

## How to stop

```bash
docker-compose down
```
 





