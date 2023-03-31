CREATE TABLE application
(
    id                       UUID PRIMARY KEY,
    insurance_sum            DECIMAL(13, 2) NOT NULL,
    registration_date        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    period_of_insurance      VARCHAR(55)    NOT NULL,
    payment_cycle            VARCHAR(55)    NOT NULL,
    policy_sum               DECIMAL(13, 2) NOT NULL,
    insurance_status         VARCHAR(55)    NOT NULL,
    region                   VARCHAR(30)    NOT NULL,
    district                 VARCHAR(30),
    city                     VARCHAR(30),
    street                   VARCHAR(30),
    house_number             VARCHAR(5),
    flat_number              VARCHAR(4),
    start_date               TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    insurance_type           VARCHAR(55)    NOT NULL,
    failure_diagnosis_report TEXT,
    CONSTRAINT start_date_check CHECK ( start_date > registration_date )
);
CREATE TABLE sum_assignment
(
    id          INTEGER PRIMARY KEY,
    is_flat     BOOLEAN        NOT NULL,
    name        VARCHAR(55)    NOT NULL,
    general_sum DECIMAL(13, 2) NOT NULL,
    sum         DECIMAL(13, 2) NOT NULL,
    min_sum     DECIMAL(13, 2) NOT NULL,
    max_sum     DECIMAL(13, 2) NOT NULL,
    default_sum DECIMAL(13, 2) NOT NULL
);
CREATE TABLE property_insurance
(
    application_id    UUID PRIMARY KEY REFERENCES application (id),
    sum_assignment_id INTEGER REFERENCES sum_assignment (id)
);

CREATE TABLE program
(
    id                           INTEGER PRIMARY KEY,
    name                         VARCHAR(30)    NOT NULL,
    sum                          DECIMAL(13, 2) NOT NULL,
    organization                 VARCHAR(30)    NOT NULL,
    link                         VARCHAR(100)   NOT NULL,
    description                  TEXT           NOT NULL,
    is_emergency_hospitalization BOOLEAN        NOT NULL,
    is_dental_service            BOOLEAN        NOT NULL,
    is_telemedicine              BOOLEAN        NOT NULL,
    is_emergency_medical_care    BOOLEAN        NOT NULL,
    is_calling_doctor            BOOLEAN        NOT NULL,
    is_outpatient_service        BOOLEAN        NOT NULL
);

CREATE TABLE medical_insurance
(
    application_id UUID PRIMARY KEY REFERENCES application (id),
    program_id     INTEGER REFERENCES program (id)
);
CREATE TABLE person
(
    id          UUID PRIMARY KEY,
    first_name  VARCHAR(30) NOT NULL,
    middle_name VARCHAR(30),
    last_name   VARCHAR(30) NOT NULL
);
CREATE TABLE insurant
(
    person_id       UUID PRIMARY KEY REFERENCES person (id),
    document_number VARCHAR(25),
    document_type   VARCHAR(55),
    email           VARCHAR(50) NOT NULL,
    phone_number    VARCHAR(11) NOT NULL,
    client_id       VARCHAR(36) NOT NULL
);
CREATE TABLE insured
(
    person_id          UUID PRIMARY KEY REFERENCES person (id),
    birthday           DATE NOT NULL,
    driving_experience VARCHAR(55) NULL,
    passport_number    VARCHAR(20)
);
CREATE TABLE factor
(
    name VARCHAR(55) PRIMARY KEY,
    rate VARCHAR(55) NOT NULL
);
CREATE TABLE base
(
    name VARCHAR(55) PRIMARY KEY,
    rate VARCHAR(55) NOT NULL
);
CREATE TABLE car_insurance
(
    application_id UUID PRIMARY KEY REFERENCES application (id),
    factor_name    VARCHAR(55) REFERENCES factor (name),
    base_name      VARCHAR(55) REFERENCES base (name)
);
CREATE TABLE OSAGO
(
    car_application_id       UUID PRIMARY KEY REFERENCES car_insurance (application_id),
    category_group           VARCHAR(55) NOT NULL,
    capacity_group           VARCHAR(55) NOT NULL,
    is_with_insured_accident BOOLEAN     NOT NULL,
    model                    VARCHAR(30) NOT NULL,
    car_number               VARCHAR(15) NOT NULL
);

CREATE TABLE KASKO
(
    car_application_id UUID PRIMARY KEY REFERENCES car_insurance (application_id)
);



CREATE TABLE agreement
(
    application_id UUID PRIMARY KEY REFERENCES application (id),
    number         VARCHAR(20),
    agreement_date DATE,
    is_active      BOOLEAN
);


CREATE TABLE person_application
(
    application_id UUID REFERENCES application (id),
    person_id      UUID REFERENCES person (id)
);