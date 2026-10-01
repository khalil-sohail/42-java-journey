CREATE TABLE users_m09_ex01 (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE messages_m09_ex01 (
    id BIGSERIAL PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    text TEXT NOT NULL,
    sending_time TIMESTAMP NOT NULL,

    CONSTRAINT fk_messages_sender FOREIGN KEY (sender_id) REFERENCES users_m09_ex01(id)
);
