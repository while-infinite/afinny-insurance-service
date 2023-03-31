CREATE TABLE travel_price
(
    id                 UUID PRIMARY KEY,
    basic_price        DECIMAL(13, 2) NOT NULL,
    insured_number     INT NOT NULL,
    is_with_sport_type BOOLEAN,
    is_with_PCR        BOOLEAN
);

CREATE TABLE travel_program
(
    id                UUID PRIMARY KEY,
    name              VARCHAR(20) NOT NULL,
    description       TEXT NOT NULL,
    final_price       DECIMAL(13, 2) NOT NULL,
    max_insurance_sum DECIMAL(13, 2) NOT NULL,
    travel_price_id   UUID REFERENCES travel_price (id)
);

CREATE TABLE travel_insurance
(
    application_id UUID PRIMARY KEY REFERENCES application (id),
    travel_program_id UUID REFERENCES travel_program (id)
);

ALTER TABLE application
    ADD COLUMN insurance_country VARCHAR(55) NOT NULL DEFAULT 'AUSTRALIA',
    ADD COLUMN sport_type VARCHAR(55),
    ADD COLUMN last_date  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
    ADD CONSTRAINT last_date_check CHECK ( last_date > application.start_date );

