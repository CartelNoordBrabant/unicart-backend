# Diff API

APIs to verify side-by-side diffs of base64 encoded JSON.

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
~/diff: docker-compose build
# After the build start up or shut down should be fast
~/diff: docker-compose up
~/diff: docker-compose down
```

### Without docker-compose

#### JDK 16

Official instructions [here](https://openjdk.java.net/install/).

After JDK 16 is available you can use the maven wrapper to build.

```sh
# Can take a couple of minutes pulling maven dependencies
~/diff: ./mvnw clean package
```

#### Redis 6.2

Download available [here](https://redis.io/download).

Or use docker to pull a redis image:

```sh
~/diff: docker run --name redis -p 6379:6379 -d redis:6.2-alpine
```

The server will connect to redis default port: **6379**

#### Running

Make sure your Redis instance is up and running.

Now just run .jar built in target dir.

```sh
~/diff: cd target
~/diff/target: java -jar diff-1.0.0.jar
```

Server should start at port: **8080**

---

## Testing the API

### OpenAPI and Swagger

A visual way to test it is visiting: http://localhost:8080/swagger-ui.html

There you will find the **API Docs** and a simple UI to make requests to your running server.

Some helper test files can be found at **test-files** dir.

```sh

~/diff: ls test-files

invalid.json  invalid_json.b64  normal.txt  normal_txt.b64  ok.json  ok_json.b64
```

Also some web helpers: [Base64 Encoder/Decoder](https://www.base64encode.org/)
, [JSON Formatter](https://jsonformatter.curiousconcept.com/), [UUID Generator](https://www.uuidgenerator.net/version4).

### API

All info can be found in Swagger, but in a nutshell:

There is only three endpoints:

- **POST** `/v1/diff/{id}/left`
- **POST** - `/v1/diff/{id}/right`
- **GET** - `/v1/diff/{id}`

Where `{id}` is a user defined `UUID` that identifies a diff.

#### **POST** `/v1/diff/{id}/left`

- Sends the left side data to be compared.
- Data should be a base64 encoded JSON.
- Returns `ACCEPTED (202)` when successful.

#### **POST** - `/v1/diff/{id}/right`

- Sends the right side data to be compared.
- Data should be a base64 encoded JSON.
- Returns `ACCEPTED (202)` when successful.

#### **GET** - `/v1/diff/{id}`

- Gets the side-by-side diff between right and left (decoded) data.
- Diff is only done if both sides are present.
- Diffs are returned in JSON format as:
    ```json
    {
        "result": "RESULT_CODE",
        "differences": [
            { "offset": 19, "length": 1  },
            { "offset": 40, "length": 20 },
            { "offset": 81, "length": 12 }
        ]
    }
   ```
- Possible results are **EQUAL**, **DIFFERENT_SIZES** and **DIFFERENT**.

---

## Technical Overview

### Tech Stack

- **Java** with **Spring Boot** and **Maven**
- **Redis**
- **OpenAPI** and **Swagger**
- **Docker** and **docker-compose**

Every tool was used in the latest version.

#### Java, Spring Boot and Maven

Since version 8 Java has been not only reliable but very productive language. With Spring Boot even more so, as this
framework has many integrations out of the box, like Maven, OpenAPI and Redis, all of it very well tested.

And speaking of tests, Spring Boot Test with JUnit 5 makes Unit and Integration tests really simple.

#### Redis

Redis is really fast and reliable, and given a transient nature of a diff application, it is really helpful.

In this project it is used as a transient datasource and for distributed caching, meaning that we could have multiple
instances of this server, and they would share a cache, which makes horizontal scaling easier. Also, Redis can be
clustered if needed. Some tweaks in **docker-compose.yaml** would be needed to make this possible.

#### OpenAPI and Swagger

OpenAPI is a standard, and Swagger is a nice UI to easily test endpoints. With Spring Boot having the capability to
generate docs from code, makes life much easier.

#### Docker and docker-compose

Since Java usually has tons of dependencies, making this server a container reduces loads of unknown variables. Docker
is a great standard for building containers.

docker-compose, was chosen as a quick and simple way to start 2 different containers in the same network. Other tools
can be used as well, but docker-compose seems simple enough for this simple case.

### Source code organization

All source code, besides `Dockerfile`, `docker-compose.yaml` and `pom.xml` resides on `src` folder, which follows a
DDD-like structure. Real DDD structure felt much bloated for this simple case, but some inspiration was drawn:

- `api` deals with classes that directly affects the API. Changes here could mean a `v2`
- `domain` all logic and internal behaviour lives here.
- `shared` some helpers are always needed.

This same structure can be found at `test` package.

### Testing strategy

#### Unit tests

Most classes has Unit tests to help speed up test/code cycles, even if they are somewhat redundant with Integration
tests. The reasoning is that you can do few quick cycles to test some specific feature/bug, without having to start up
the entire application.

#### Integration tests

Integration tests are done at 2 levels:

- Repository: To test datasource integration.
- API: To test the application as a whole.

Some level of test redundancy can be viewed as over testing, but I personally find over testing better than under
testing. This position could be reevaluated if tests becomes a real burden.

#### Random test data

Most test cases uses pseudo-random data to improve test reliability (each run uses different data), and improve
readability in most cases. In cases where readability could be affected, explicit data is used.

---

## Improvements

Some next steps in no particular order:

- Use **testcontainers** for integration tests. At the moment integration tests requires a redis instance running at **
  localhost:6379**.
- Accepting file uploads and computing diffs asynchronously, that would make diffs of big files possible.
- Improve OpenAPI with examples, instead of a `test-file` dir, some default data could be added for use.
- Use a real diff algorithm like the
  [Longest Common Subsequence](https://en.wikipedia.org/wiki/Longest_common_subsequence_problem).
- Add server metrics to understand how it is being used and validate assumptions.
