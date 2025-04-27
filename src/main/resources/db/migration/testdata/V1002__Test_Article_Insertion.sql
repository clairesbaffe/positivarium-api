INSERT INTO articles
(title, content, main_image, is_published, published_at, user_id)
VALUES('title', 'content', 'https://unsplash.com/fr/photos/un-mouton-brun-et-blanc-assis-au-sommet-dune-colline-verdoyante-ltO77p_AcYc', false, null, 1);

INSERT INTO comments
(content, user_id, article_id)
VALUES('super article !', 3, 1);