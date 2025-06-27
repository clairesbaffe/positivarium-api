INSERT INTO daily_news_preferences (user_id, journal_entry_id) VALUES
(3, 1),
(5, 2),
(7, 3),
(9, 4);

INSERT INTO daily_news_preferences_categories (category_id, daily_news_preference_id) VALUES
(3, 1),     -- Société
(6, 1),     -- Musique
(19, 1),    -- Mode & beauté
(25, 1),    -- Bien-être & mental

(4, 2),     -- Environnement
(8, 2),     -- Arts & design
(20, 2),    -- Voyage

(23, 3),    -- Entrepreunariat
(24, 3),    -- Productivité

(3, 4),     -- Société
(7, 4),     -- Littérature
(17, 4),    -- Santé & médecine
(22, 4);    -- Education

INSERT INTO global_news_preferences (mood_id, user_id) VALUES
(1, 3),     -- Euphorie
(10, 3),    -- Découragé
(30, 3),    -- Emerveillé
(36, 9);    -- Sûr de soi

INSERT INTO global_news_preferences_categories (category_id, global_news_preference_id) VALUES
(18, 1),    -- Jeux vidéo
(20, 1),    -- Voyage
(21, 1),    -- Gastronomie
(6, 2),     -- Musique
(20, 2),    -- Voyage
(25, 2),    -- Bien-être & mental
(14, 3),    -- Intelligence artificielle
(16, 3),    -- Espace & astronomie
(9, 4),     -- Football
(12, 4);    -- Formule 1