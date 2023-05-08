CREATE TABLE "person_event"
(
    id              UUID PRIMARY KEY    NOT NULL,
    aggregate_id    UUID                NOT NULL,
    type            PERSON_EVENT_TYPE NOT NULL,
    occurred_on     TIMESTAMP           NOT NULL,
    additional_data JSON
);