ALTER TABLE car_insurance
    DROP COLUMN factor_name;

ALTER TABLE base
    ADD factor_name VARCHAR(55) REFERENCES factor (name);