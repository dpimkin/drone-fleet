FROM openjdk:17-alpine
MAINTAINER dpimkin.it@gmail.com
RUN addgroup -S appuser && adduser -S appuser -G appuser
USER appuser:appuser
ARG JAR_FILE
ADD ${JAR_FILE} /app/drone-fleet.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev","-jar","/app/drone-fleet.jar"]