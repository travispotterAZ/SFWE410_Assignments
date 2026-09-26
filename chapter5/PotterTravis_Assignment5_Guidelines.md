# Assignment 5 Submission

This folder contains the Chapter 5 licensing service, config server, and Postman collection for the SFWE410 Assignment 5, which adds a Spring Cloud Config Server to the Chapter 4 licensing service and externalizes the service's configuration.

## What changed for this assignment

All three changes below are made through Docker Compose environment variables in `docker/docker-compose.yml` — none of the application's `bootstrap.yml`/`application.properties` source files were edited.

### Step 2 — Config server runs on port 8089

`configserver`'s `bootstrap.yml` still hardcodes `server.port: 8071`, but the `configserver` service in `docker-compose.yml` sets:

```yaml
SERVER_PORT: "8089"
```

Spring Boot maps `SERVER_PORT` to `server.port` automatically, and environment variables take precedence over the value baked into the jar, so the container serves on `8089` (mapped to host port `8089`) without any source change.

### Step 3 — New `test` profile for licensing-service

A new profile-specific properties file was added at `configserver/src/main/resources/config/licensing-service-test.properties`, alongside the existing `-dev` and `-prod` variants. Spring Cloud Config matches profiles to files by the `{application}-{profile}.properties` naming convention. Its Postgres password is set to `pass`:

```properties
spring.datasource.password = pass
```

The `licensingservice` service in `docker-compose.yml` sets:

```yaml
SPRING_PROFILES_ACTIVE: "test"
```

which overrides the `dev` profile hardcoded in licensing-service's own `bootstrap.yml`, so at startup it requests and receives `licensing-service-test.properties` from the config server instead of the dev config.

### Step 4 — Config server integrated with git

`configserver`'s `bootstrap.yml` has its `git:` block commented out; instead, `docker-compose.yml` switches the config server from local (`native`) config to git-backed config:

```yaml
SPRING_PROFILES_ACTIVE: "git"
SPRING_CLOUD_CONFIG_SERVER_GIT_URI: "https://github.com/travispotterAZ/SFWE410_Assignments.git"
SPRING_CLOUD_CONFIG_SERVER_GIT_SEARCHPATHS: "chapter5/configserver/src/main/resources/config"
SPRING_CLOUD_CONFIG_SERVER_GIT_DEFAULTLABEL: "master"
```

Instead of reading the properties files bundled into its own container image, the config server now clones/pulls this repository at startup and serves the `licensing-service*.properties` files directly out of the `chapter5/configserver/src/main/resources/config` path on the `master` branch. Since the repository is public, no git credentials are required.

## Prerequisites

- Apache Maven (http://maven.apache.org)
- Docker Desktop (includes Docker Engine, CLI, and Compose)
- Postman
- The Chapter 5 source code in `PotterTravis_Assignment5_Code/` (included in this submission as `PotterTravis_Assignment5_Code.zip`)

## Run the stack

Unzip `PotterTravis_Assignment5_Code.zip`, then run:

```powershell
cd PotterTravis_Assignment5_Code/configserver
mvn clean package dockerfile:build -DskipTests

cd ../licensing-service
mvn clean package dockerfile:build -DskipTests

cd ../docker
docker-compose up -d
```

> **Note:** `-DskipTests` is required for `configserver`. Its `bootstrap.yml` activates the `git` profile by default but leaves the `git:` uri block commented out (a pre-existing issue in the original textbook starter code, unrelated to this assignment's changes). Without `-DskipTests`, the build's `ConfigurationServerApplicationTests` test boots the app locally with no git URI available and fails with `IllegalStateException: You need to configure a uri for the git repository.` This only affects the local Maven test run — the actual container gets its git URI from `docker-compose.yml`'s `SPRING_CLOUD_CONFIG_SERVER_GIT_URI` environment variable and works correctly.

Docker Compose's `depends_on` conditions handle startup order automatically:

1. `database` (Postgres) and `configserver` start first (no dependencies on each other).
2. `licensingservice` waits until `database` reports healthy and `configserver` has started, then starts.

Check everything is up:

```powershell
docker-compose ps
```

## Verify the config server is serving the `test` profile from git

```powershell
curl http://localhost:8089/licensing-service/test
```

This should return JSON built from `licensing-service-test.properties`, including `spring.datasource.password: pass`, confirming the config server is resolving the `test` profile from the git-backed source.

## Use the Postman collection

1. Open Postman.
2. Select **Import** and choose `PotterTravis_Assignment5_PostmanCollection.postman_collection.json`.
3. Open the imported collection and confirm its variables:
   - `baseUrl`: `http://localhost:8080`
   - `organizationId`: `e6a625cc-718b-48c2-ac76-1dfdff9a531e` (must be a real organization id already seeded by `data.sql`, since `licenses.organization_id` has a foreign key to `organizations`; other valid ids in the seed data are `d898a142-de44-466c-8c88-9ceb2c2429d3` and `e839ee96-28de-4f67-bb79-870ca89743a0`)
   - `licenseId`: `test-license-1` (only used as the initial placeholder — see note below)
4. Run the **License CRUD** group in order:
   1. Create License
   2. Get License
   3. Update License
   4. Delete License
   5. Get License (Not Found) — confirms the row was actually removed from Postgres (expects HTTP 404)

> **Note:** `LicenseService.createLicense()` always overwrites the submitted `licenseId` with a server-generated `UUID.randomUUID()` before saving — the `test-license-1` value sent in the Create request body is discarded. To keep the rest of the sequence working, **Create License** has a test script that reads the real `licenseId` back from the response and stores it in the `licenseId` collection variable, so Get/Update/Delete automatically operate on the license that was actually created.

## Stopping the stack

```powershell
docker-compose down -v
```

This stops all three containers and removes the Postgres data volume, so the database starts empty next time.
