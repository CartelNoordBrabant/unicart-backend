# UniCart Backend

APIs to handle the shared shopping cart.

---

#### Summary

* [Running the server](#running-the-server)
* [Testing the API](#testing-the-api)
* [Technical Overview](#technical-overview)
* [Improvements](#improvements)

---

## Running the server

Using **docker-compose** is the easiest and more straightforward way to get this server up, as it would be the only
dependency.

If you don't want to use **docker-compose** you'll need Java 16 installed and Redis 6.2 running locally.

Both methods are described bellow, and both will need you to clone this repo.

```sh
~: git clone git@github.com:felipeguilhermefs/diff.git
~: cd diff
```

### With docker-compose (recommended)

If you don't have **docker-compose** you can find official installation
instructions [here](https://docs.docker.com/compose/install/).

After you have **docker-compose** available it is just build and run:

```sh
# Can take a couple of minutes pulling maven dependencies
~/unicart-backend: docker-compose build
# After the build start up or shut down should be fast
~/unicart-backend: docker-compose up
~/unicart-backend: docker-compose down
```

### Without docker-compose

#### JDK 16

Official instructions [here](https://openjdk.java.net/install/).

After JDK 16 is available you can use the maven wrapper to build.

```sh
# Can take a couple of minutes pulling maven dependencies
~/unicart-backend: ./mvnw clean package
```

#### Redis 6.2

Download available [here](https://redis.io/download).

Or use docker to pull a redis image:

```sh
~/unicart-backend: docker run --name redis -p 6379:6379 -d redis:6.2-alpine
```

The server will connect to redis default port: **6379**

#### Running

Make sure your Redis instance is up and running.

Now just run .jar built in target dir.

```sh
~/unicart-backend: cd target
~/unicart-backend/target: java -jar unicart-1.0.0.jar
```

Server should start at port: **8080**

---

## Testing the API

### OpenAPI and Swagger

A visual way to test it is visiting: http://localhost:8080/swagger-ui.html

There you will find the **API Docs** and a simple UI to make requests to your running server.
