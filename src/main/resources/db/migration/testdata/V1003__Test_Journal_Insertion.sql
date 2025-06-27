INSERT INTO moods (name, type) VALUES
('Euphorie', 'Joie'),
('Heureux', 'Joie'),
('Content', 'Joie'),
('Excité', 'Joie'),
('Amoureux', 'Joie'),
('Reconnaissant', 'Joie'),
('Triste', 'Tristesse'),
('Déprimé', 'Tristesse'),
('Mélancolique', 'Tristesse'),
('Découragé', 'Tristesse'),
('Solitaire', 'Tristesse'),
('Lamentable', 'Tristesse'),
('Fâché', 'Colère'),
('Enragé', 'Colère'),
('Irrité', 'Colère'),
('Frustré', 'Colère'),
('Exaspéré', 'Colère'),
('Honteux', 'Colère'),
('Anxieux', 'Peur'),
('Inquiet', 'Peur'),
('Effrayé', 'Peur'),
('Terrifié', 'Peur'),
('Paniqué', 'Peur'),
('Craintif', 'Peur'),
('Surpris', 'Surprise'),
('Stupéfait', 'Surprise'),
('Etonné', 'Surprise'),
('Terrifié', 'Surprise'),
('Paniqué', 'Surprise'),
('Emerveillé', 'Surprise'),
('Dégoûté', 'Dégoût'),
('Révolté', 'Dégoût'),
('Ecoeuré', 'Dégoût'),
('Horrifié', 'Dégoût'),
('Confiant', 'Confiance'),
('Sûr de soi', 'Confiance'),
('Optimiste', 'Confiance'),
('Satisfait', 'Confiance'),
('Impatient', 'Anticipation'),
('Curieux', 'Anticipation'),
('Attentif', 'Anticipation'),
('Motivé', 'Anticipation'),
('Espérant', 'Anticipation');

INSERT INTO journal_entries (description, user_id) VALUES
('Aujourd’hui, j’ai pris le temps de noter toutes les petites choses qui m’ont fait sourire, comme le chant des oiseaux au matin et les échanges chaleureux avec mes collègues. Ces instants simples mais précieux me rappellent à quel point la vie est belle même dans les moments ordinaires.', 3),
('Je me suis sentie un peu fatiguée et mélancolique ce matin, mais écrire dans mon journal m’a permis de relativiser. Je suis reconnaissante pour le soutien de mes amis et le soleil qui a percé les nuages cet après-midi.', 5),
('J’ai passé la journée à explorer de nouveaux livres inspirants qui m’ont donné de la motivation pour mes projets personnels. Je ressens une grande satisfaction d’apprendre et de grandir chaque jour.', 7),
('Malgré quelques soucis au travail, j’ai trouvé un moment pour exprimer ma gratitude envers ma famille et les petites victoires de la journée. Cette réflexion m’a redonné confiance et optimisme pour la suite.', 9);


INSERT INTO journal_entries_moods (journal_entry_id, mood_id) VALUES
(1, 2),  -- Heureux
(1, 6),  -- Reconnaissant

(2, 9),  -- Mélancolique
(2, 6),  -- Reconnaissant

(3, 34), -- Satisfait
(3, 37), -- Motivé

(4, 32), -- Confiant
(4, 33), -- Sûr de soi
(4, 35); -- Optimiste