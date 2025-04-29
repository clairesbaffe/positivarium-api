INSERT INTO articles
(title, content, main_image, is_published, published_at, user_id)
VALUES('title', 'content', 'https://unsplash.com/fr/photos/un-mouton-brun-et-blanc-assis-au-sommet-dune-colline-verdoyante-ltO77p_AcYc', false, null, 1);

INSERT INTO comments
(content, user_id, article_id)
VALUES('super article !', 3, 1);

INSERT INTO general_categories (name) VALUES
('Actualités générales'),
('Médias & Culture'),
('Sport'),
('Technologies & Sciences'),
('Divertissement & Lifestyle'),
('Développement personnel');

INSERT INTO news_categories (name, category_id) VALUES
('Politique', 1),
('Economie', 1),
('Société', 1),
('Environnement', 1),
('Cinéma & séries', 2),
('Musique', 2),
('Littérature', 2),
('Arts & design', 2),
('Football', 3),
('Basketball', 3),
('Tennis', 3),
('Formule 1', 3),
('Autres sports', 3),
('Intelligence artificielle', 4),
('Startups & innovation', 4),
('Espace & astronomie', 4),
('Santé & médecine', 4),
('Jeux vidéo', 5),
('Mode & beauté', 5),
('Voyage', 5),
('Gastronomie', 5),
('Education', 5),
('Entrepreunariat', 5),
('Productivité', 5),
('Bien-être & mental', 5);