# Pre-requisites
  - Maven
  - Docker



# How to run
This application uses MS SQL Server. To set it up, go the the project directory then run the following:

```sh
$ docker-compose up
```

Then, using your favorite sql client, create the database. (You'll find the default credentials inside the docker-compose.yaml file)
```sh
$ CREATE DATABASE VACCNOW
```

Run the application:
```sh
$ mvn spring-boot:run
```

# API Usage
Swagger documentation is available at:
```sh
$ http://localhost:8060/swagger-ui.html
```

# Relationship Diagram
![Alt text](erd.png?raw=true "ERD")
