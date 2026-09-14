# Assignment 2 - Chapter 2 (Licensing Service)

Project: `manning-smia/chapter2/licensing-service`

## Files Changed / Added

### src/main/resources/application.properties
Configured an in-memory H2 database, disabled Hibernate auto-DDL
(`ddl-auto=none`) so `schema.sql`/`data.sql` control the schema.

### src/main/resources/schema.sql (new)
Creates the `person`, `organization`, and `person_organization` tables that
back the domain models, matching the multiplicities in the class diagram.

### src/main/resources/data.sql (new)
Sample data: 6 people and 4 organizations (one per `Category`), each
organization with a president and one or more members.

### src/main/java/.../model/Category.java (new)
The `<<enumeration>>` from the diagram: `SPORTS`, `FITNESS`, `ARTS`,
`LITERATURE`.

### src/main/java/.../model/Person.java (new)
Domain model for `Person` (name, major, dept, dateOfBirth, phone, email).
Holds `organizationsPresided` (one-to-many, "president of") and
`organizations` (many-to-many, "member of").

### src/main/java/.../model/Organization.java (new)
Domain model for `Organization` (name, category, establishedDate). Holds a
`president` (many-to-one) and `members` (many-to-many, backed by the
`person_organization` join table).

### src/main/java/.../repository/PersonRepository.java (new)
Spring Data JPA repository for `Person`.

### src/main/java/.../repository/OrganizationRepository.java (new)
Spring Data JPA repository for `Organization`, with a `findAllWithMembers()`
query that eagerly fetches each organization's members.

### src/test/java/.../repository/PersonRepositoryTest.java (new)
JUnit test that loads all `Person` records and prints each one's full info.

### src/test/java/.../repository/OrganizationRepositoryTest.java (new)
JUnit test that loads all `Organization` records and prints a report of
each one's category, president, and number of student members.

## Steps for Testing

From `licensing-service/`, run:

```
mvn test -Dtest=PersonRepositoryTest
mvn test -Dtest=OrganizationRepositoryTest
```
