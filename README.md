# Build

System requirements:

* jdk 17
* docker

```bash
./mvnw clean install
```

# How to run

Run following command:

```bash
docker run -p 8088:8088 -p 9099:9099 -ti dpimkin/drone-fleet:1.0.0 
```
And then go to [Swagger UI](http://localhost:8088/swagger-ui.html)
 





