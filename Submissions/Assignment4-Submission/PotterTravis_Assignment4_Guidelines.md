# Assignment 4 Postman Submission

This folder contains the Postman collection for the SFWE410 Chapter 4 licensing service assignment, which adds Docker Compose, an external Postgres database, and a Docker image build to the Chapter 3 licensing service.

## Prerequisites

- Docker Desktop (includes Docker Engine, CLI, and Compose)
- Postman
- The Chapter 4 licensing service source code in `PotterTravis_Assignment4_Code/licensing-service` (included in this submission as `PotterTravis_Assignment4_Code.zip`)

## Run the licensing service

Unzip `PotterTravis_Assignment4_Code.zip`, then run:

```powershell
cd PotterTravis_Assignment4_Code/licensing-service
docker compose up --build
```

This builds the licensing-service Docker image (via the `dockerfile-maven-plugin`-configured `Dockerfile`), starts a Postgres container named `database`, and starts the `licensingservice` container, which waits for Postgres to report healthy before connecting. The service should start on `http://localhost:8080`. Keep this terminal running while using Postman.

Postgres is exposed on `localhost:5432` (database `ostock_dev`, user `postgres`, password `postgres`) if you want to inspect the data directly with a Postgres client (e.g., the Microsoft PostgreSQL extension for VS Code) while testing.

## Use the Postman collection

1. Open Postman.
2. Select **Import** and choose `PotterTravis_Assignment4_PostmanCollection.postman_collection.json`.
3. Open the imported collection and confirm its variables:
   - `baseUrl`: `http://localhost:8080`
   - `organizationId`: `test-org-1`
   - `licenseId`: `test-license-1`
4. Run the **License CRUD** group in order:
   1. Create License
   2. Get License
   3. Update License
   4. Delete License
   5. Get License (Not Found) — confirms the row was actually removed from Postgres (expects HTTP 404)

The collection does not require a separate Postman environment. If the service uses a different host or port, update the `baseUrl` collection variable before sending requests.

## What the collection contains

The collection tests the licensing service endpoint:

```text
/v1/organization/{organizationId}/license
```

It contains one request group, **License CRUD**, with Create, Get, Update, Delete, and a follow-up Get that verifies deletion. Create and Update send a JSON license containing the organization, description, product name, and license type. Every request in this sequence was run against the live Docker Compose stack (licensing-service + Postgres) to confirm the data round-trips through the database: Create persists a row, Get returns it, Update changes it and Get reflects the change, Delete removes it, and the final Get returns HTTP 404.

## Stopping the stack

```powershell
docker compose down -v
```

This stops both containers and removes the Postgres data volume, so the database starts empty next time.
