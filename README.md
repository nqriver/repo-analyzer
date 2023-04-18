# About
This project exposes a REST API endpoint to fetch github repository details of a given user and repo name.

# Technologies
* Java 17
* Gradle
* Spring Boot v. 3.0.5
* Docker/docker-compose

# How to run

* Build project using gradle or gradle wrapper
```bash
./gradlew clean build
```
  
* Build docker image and run the container
```bash
docker-compose up -d
```
  
* Verify the container is up and running
```bash
docker-compose ps
```

You should see something similar to this:
```bash
Name                Command        State           Ports
-------------------------------------------------------------------------
recruitment_my-app_1   java -jar app.jar   Up      0.0.0.0:8080->8080/tcp
```

* Hit the endpoint with sample data
Existing repo
```bash
curl http://localhost:8080/repositories/ddd-by-examples/library
```

Nonexistent repo
```bash
curl http://localhost:8080/repositories/imaginary-user/imaginary-repo
```

* Finally, stop the container
```bash
docker-compose down
```