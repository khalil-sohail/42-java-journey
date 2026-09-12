
INSERT INTO users (login, password) VALUES
('user1', 'password1'),
('user2', 'password2'),
('user3', 'password3'),
('user4', 'password4'),
('user5', 'password5');

INSERT INTO chatrooms (name, owner_id) VALUES
('room1', 1),
('room2', 1),
('room3', 2),
('room4', 3),
('room5', 4);

INSERT INTO users_chatrooms (user_id, room_id) VALUES
(1, 1),
(1, 2),
(2, 1),
(3, 2),
(4, 5);