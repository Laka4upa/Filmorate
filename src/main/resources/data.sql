-- mpa_ratings
INSERT INTO mpa_ratings (mpa_id, name) VALUES
    (1, 'G'),
    (2, 'PG'),
    (3, 'PG-13'),
    (4, 'R'),
    (5, 'NC-17');

-- genres
INSERT INTO genres (genre_id, name) VALUES
(1, 'Комедия'),
(2, 'Драма'),
(3, 'Мультфильм'),
(4, 'Триллер'),
(5, 'Документальный'),
(6, 'Боевик');

-- users
INSERT INTO users (user_id, email, login, name, birthday) VALUES
    (1, 'user1@example.com', 'user1', 'User One', '1990-01-01'),
    (2, 'user2@example.com', 'user2', 'User Two', '1985-05-05'),
    (3, 'user3@example.com', 'user3', 'User Three', '2000-10-10');


-- films
INSERT INTO films (film_id, name, description, release_date, duration, mpa_id) VALUES
    (1, 'Film A', 'Description of Film A', '2020-01-01', 120, 3),
    (2, 'Film B', 'Description of Film B', '2021-02-15', 95, 2),
    (3, 'Film C', 'Description of Film C', '2019-07-20', 110, 4),
    (4, 'Cartoon X', 'Animated adventure.', '2015-03-10', 80, 1),
    (5, 'Cartoon Y', 'Fun and colorful animation.', '2018-09-05', 75, 1);

-- film_genre
INSERT INTO film_genre (film_id, genre_id) VALUES
    (1, 2), -- Film A - Drama
    (1, 5), -- Film A - Sci-Fi
    (2, 1), -- Film B - Comedy
    (3, 4), -- Film C - Horror
    (4, 6), -- Cartoon X - Animation
    (5, 6); -- Cartoon Y - Animation

-- likes
INSERT INTO likes (film_id, user_id) VALUES
    (1, 1),
    (1, 2),
    (2, 2),
    (4, 3),
    (5, 1);

-- friendships
INSERT INTO friendships (user_id, friend_id, status) VALUES
(1, 2, 'confirmed'),
(1, 3, 'not_confirm'),
(2, 3, 'confirmed');
