CREATE TABLE rooms_m09_ex02 (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE users_m09_ex02 (
    id BIGSERIAL PRIMARY KEY,
    last_room_id BIGINT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,

    CONSTRAINT fk_users_last_room
        FOREIGN KEY (last_room_id)
        REFERENCES rooms_m09_ex02(id)
);

CREATE TABLE messages_m09_ex02 (
    id BIGSERIAL PRIMARY KEY,

    room_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    
    text TEXT NOT NULL,
    sending_time TIMESTAMP NOT NULL,

    CONSTRAINT fk_messages_room
        FOREIGN KEY (room_id)
        REFERENCES rooms_m09_ex02(id),

    CONSTRAINT fk_messages_sender
        FOREIGN KEY (sender_id)
        REFERENCES users_m09_ex02(id)
);

CREATE TABLE users_rooms_m09_ex02 (
    user_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, room_id),

    CONSTRAINT fk_users_rooms_user
        FOREIGN KEY (user_id)
        REFERENCES users_m09_ex02(id),

    CONSTRAINT fk_users_rooms_room
        FOREIGN KEY (room_id)
        REFERENCES rooms_m09_ex02(id)
);