TRUNCATE TABLE users_chatrooms, messages, chatrooms, users
RESTART IDENTITY CASCADE;


INSERT INTO users (login, password) VALUES
('user1', 'password1'),
('user2', 'password2'),
('user3', 'password3'),
('user4', 'password4'),
('user5', 'password5'),
('user6', 'password6'),
('user7', 'password7'),
('user8', 'password8'),
('user9', 'password9'),
('user10', 'password10');


INSERT INTO chatrooms (name, owner_id) VALUES
('room1', 1),
('room2', 1),
('room3', 2),
('room4', 3),
('room5', 4),
('room6', 5),
('room7', 6),
('room8', 7),
('room9', 8),
('room10', 9);


INSERT INTO messages (room_id, author_id, text, date_time) VALUES
(1, 1, 'Hello, world!', '2026-09-14 10:00:00'),
(2, 2, 'This is a test message.', '2026-09-14 10:05:00'),
(3, 3, 'Another message.', '2026-09-14 10:10:00'),
(4, 4, 'Yet another message.', '2026-09-14 10:15:00'),
(5, 5, 'Hello from user 5.', '2026-09-14 10:20:00'),
(6, 6, 'This is a message from user 6.', '2026-09-14 10:25:00'),
(7, 7, 'User 7 here!', '2026-09-14 10:30:00'),
(8, 8, 'Message from user 8.', '2026-09-14 10:35:00'),
(9, 9, 'User 9 checking in.', '2026-09-14 10:40:00'),
(10, 10, 'Final message from user 10.', '2026-09-14 10:45:00'),
(1, 2, 'User 2 in room 1.', '2026-09-14 10:50:00'),
(2, 3, 'User 3 in room 2.', '2026-09-14 10:55:00'),
(3, 4, 'User 4 in room 3.', '2026-09-14 11:00:00'),
(4, 5, 'User 5 in room 4.', '2026-09-14 11:05:00'),
(5, 6, 'User 6 in room 5.', '2026-09-14 11:10:00'),
(6, 7, 'User 7 in room 6.', '2026-09-14 11:15:00'),
(7, 8, 'User 8 in room 7.', '2026-09-14 11:20:00'),
(8, 9, 'User 9 in room 8.', '2026-09-14 11:25:00'),
(9, 10, 'User 10 in room 9.', '2026-09-14 11:30:00'),
(10, 1, 'User 1 back in room 10.', '2026-09-14 11:35:00');


INSERT INTO users_chatrooms (user_id, room_id) VALUES
(1, 1),
(1, 2),
(2, 1),
(3, 2),
(4, 3),
(5, 4),
(6, 5);