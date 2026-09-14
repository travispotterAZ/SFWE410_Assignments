DROP TABLE IF EXISTS person_organization;
DROP TABLE IF EXISTS organization;
DROP TABLE IF EXISTS person;

CREATE TABLE person (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(100) NOT NULL,
    major          VARCHAR(100),
    dept           VARCHAR(100),
    date_of_birth  DATE,
    phone          VARCHAR(20),
    email          VARCHAR(100)
);

CREATE TABLE organization (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    name              VARCHAR(100) NOT NULL,
    category          VARCHAR(20)  NOT NULL,
    established_date  DATE,
    president_id      BIGINT,
    CONSTRAINT fk_organization_president
        FOREIGN KEY (president_id) REFERENCES person (id)
);

-- Join table implementing the many-to-many "member of" association
CREATE TABLE person_organization (
    person_id        BIGINT NOT NULL,
    organization_id  BIGINT NOT NULL,
    PRIMARY KEY (person_id, organization_id),
    CONSTRAINT fk_po_person
        FOREIGN KEY (person_id) REFERENCES person (id),
    CONSTRAINT fk_po_organization
        FOREIGN KEY (organization_id) REFERENCES organization (id)
);
