insert into "user" (login, password)
values
    ('john', 'password123'),
    ('sarah', 'pass456'),
    ('mike', 'abc123'),
    ('jessica', '654321'),
    ('chris', 'secret'),
    ('matthew', 'p@ssw0rd'),
    ('emily', 'emily123'),
    ('mark', 'mark123');

insert into chatroom (name, owner)
values
    ('general', 1),
    ('book club', 2),
    ('sports', 3),
    ('coding', 4),
    ('music', 5),
    ('movies', 6),
    ('food', 7),
    ('pets', 8);

insert into "message" (author, room, text, datetime)
values
    (1, 1, 'Hello everyone!', '2023-01-01 08:00:00'),
    (2, 2, 'What book should we read next?', '2023-01-02 09:30:00'),
    (3, 3, 'Who do you think will win the game tonight?', '2023-01-03 14:00:00'),
    (4, 4, 'Need help with this Java code', '2023-01-04 11:15:00'),
    (5, 5, 'What are your favorite bands?', '2023-01-05 16:45:00'),
    (6, 6, 'Anyone seen the new Marvel movie yet?', '2023-01-06 19:30:00'),
    (7, 7, 'What should I make for dinner tonight?', '2023-01-07 18:00:00'),
    (8, 8, 'Check out my new puppy!', '2023-01-08 13:45:00');
