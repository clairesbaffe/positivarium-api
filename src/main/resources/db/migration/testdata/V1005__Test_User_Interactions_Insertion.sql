INSERT INTO follows (user_id, publisher_id) VALUES
(3, 4),
(3, 8),
(5, 2),
(9, 2),
(9, 4),
(9, 10);

INSERT INTO publisher_requests (status, motivation, user_id) VALUES
('REJECTED', $$
Je souhaite devenir rédacteur sur cette application car je crois profondément en l’importance de partager des nouvelles positives. Dans un monde souvent dominé par les mauvaises nouvelles, j’ai envie de contribuer à un espace qui fait du bien, qui inspire et qui redonne espoir.

J’aime écrire, et je suis particulièrement sensible aux initiatives solidaires, aux progrès environnementaux, aux réussites locales et aux petites histoires humaines qui font la différence. J’aimerais pouvoir relayer ces belles choses, les rendre visibles, et participer activement à cette dynamique bienveillante que vous avez lancée.

Merci de considérer ma demande, et bravo pour ce projet porteur de sens.
$$, 7),
('PENDING', $$
Chaque jour, je prends quelques minutes pour consulter cette application, et elle est rapidement devenue une vraie bouffée d’air frais dans ma routine. Lire des nouvelles qui font sourire, qui donnent de l’espoir ou qui mettent en valeur le meilleur de l’humain, ça change tout.

Aujourd’hui, j’aimerais passer de lecteur à contributeur. Je suis quelqu’un de curieux, attentif aux belles initiatives, et j’ai envie de mettre ma plume au service de cette belle mission. J’ai déjà plusieurs idées d’articles, des sujets inspirants que j’aimerais partager avec la communauté.

Contribuer à faire rayonner les bonnes nouvelles, c’est une démarche qui me tient à cœur — et je serais très heureux(se) de pouvoir le faire ici, à vos côtés.
$$, 7),
('APPROVED', $$
Je suis super enthousiaste à l’idée de rejoindre les rédacteurs de l’application. J’adore le concept de partager uniquement des bonnes nouvelles — c’est exactement le type de contenu dont on a besoin aujourd’hui.

J’ai souvent l’œil pour repérer des infos positives qui passent inaperçues : innovations, projets locaux, gestes solidaires… J’aimerais pouvoir les mettre en lumière ici et contribuer à faire grandir cette communauté optimiste.

Écrire est pour moi un vrai plaisir, et si je peux en plus apporter du positif aux lecteurs, c’est encore mieux. Je suis motivé(e), sérieux(se) et toujours prêt(e) à apprendre pour proposer du contenu de qualité.
$$, 10),
('CANCELLED', $$
Je suis convaincu(e) que les mots ont le pouvoir de réconforter, d’inspirer et de rassembler. C’est pour cette raison que j’aimerais devenir rédacteur sur votre application.

J’ai toujours été attiré(e) par les belles histoires : celles qui montrent des élans de solidarité, des réussites personnelles ou des projets qui ont un impact positif. J’aimerais pouvoir les raconter ici, pour contribuer à créer un espace où l’on respire un peu mieux, même quelques minutes par jour.

Partager des ondes positives à travers l’écriture me semble être un engagement à la fois simple et profondément utile. Je serais honoré(e) de pouvoir y participer.
$$, 3);