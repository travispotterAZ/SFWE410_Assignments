# Assignment 3 Postman Submission

This folder contains the Postman collection for the SFWE410 Chapter 3 licensing service assignment.

## Prerequisites

- Java 11
- Apache Maven
- Postman
- The Chapter 3 licensing service source code in `PotterTravis_Assignment3_Code/licensing-service` (included in this submission as `PotterTravis_Assignment3_Code.zip`)

## Run the licensing service

Unzip `PotterTravis_Assignment3_Code.zip`, then run:

```powershell
cd PotterTravis_Assignment3_Code/licensing-service
mvn spring-boot:run
```

The service should start on `http://localhost:8080`. Keep this terminal running while using Postman.

## Use the Postman collection

1. Open Postman.
2. Select **Import** and choose `PotterTravis_Assignment3_PostmanCollection.postman_collection.json`.
3. Open the imported collection and confirm its variables:
   - `baseUrl`: `http://localhost:8080`
   - `organizationId`: `optimaGrowth`
   - `licenseId`: `0235431845`
4. Run requests individually, or run a complete CRUD group in this order:
   1. Create License
   2. Get License
   3. Update License
   4. Delete License

The collection does not require a separate Postman environment. If the service uses a different host or port, update the `baseUrl` collection variable before sending requests.

## What the collection contains

The collection tests the licensing service endpoint:

```text
/v1/organization/{organizationId}/license
```

It contains four request groups:

- **Initial Requests**: Create, read, update, and delete requests without an `Accept-Language` header.
- **Requests (en)**: CRUD requests with `Accept-Language: en`.
- **Requests (es)**: CRUD requests with `Accept-Language: es`.
- **Requests (fr)**: CRUD requests with `Accept-Language: fr`.

The create and update requests send a JSON license containing the organization, description, product name, and license type. The language groups verify that the service returns localized response messages for English, Spanish, and French.

Because the language groups use the same collection variables, run each group as a complete CRUD sequence, or reset/recreate the test data between groups when necessary. The delete request removes the test license from the service.
