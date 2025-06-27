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



INSERT INTO articles
(title, description, content, main_image, category_id, is_published, published_at, user_id)
VALUES
('Les avancées prometteuses en intelligence artificielle',
 'Focus sur les dernières innovations en IA qui améliorent notre quotidien.',
 $$
 L’intelligence artificielle (IA) ne cesse de progresser à une vitesse impressionnante, transformant peu à peu notre quotidien de manière positive. Ces dernières années, de nombreuses innovations ont vu le jour, rendant les technologies plus accessibles, efficaces et bénéfiques pour tous.

 ## Amélioration des soins de santé

 Grâce à l’IA, le diagnostic médical devient plus rapide et précis. Des algorithmes sont capables d’analyser des images médicales pour détecter des maladies à un stade précoce, facilitant ainsi un traitement plus efficace. Par exemple, l’IA aide désormais à repérer certains cancers ou troubles neurologiques bien avant qu’ils ne deviennent critiques.

 ## Optimisation de la vie quotidienne

 Les assistants personnels intelligents, comme les applications de gestion d’agenda ou de domotique, s’adaptent de mieux en mieux à nos habitudes. Ils rendent nos maisons plus confortables et économes en énergie, tout en nous aidant à mieux organiser nos journées.

 ## Environnement et développement durable

 L’IA joue également un rôle clé dans la lutte contre le changement climatique. Elle permet d’optimiser la gestion des ressources, de prévoir les catastrophes naturelles avec plus de précision et d’aider à concevoir des solutions écologiques innovantes.

 ## Vers un futur collaboratif

 Les chercheurs et ingénieurs travaillent à rendre l’IA plus éthique et transparente, afin qu’elle puisse être utilisée de manière responsable au bénéfice de tous. L’objectif est de créer un partenariat entre humains et machines, où l’IA amplifie nos capacités sans remplacer notre humanité.

 ---

 L’intelligence artificielle est donc une force positive en pleine expansion, qui, bien utilisée, peut contribuer à un monde meilleur, plus sain et plus connecté. Restez curieux, car les innovations à venir promettent de nous surprendre encore davantage !
 $$,
 'https://images.pexels.com/photos/8386440/pexels-photo-8386440.jpeg', 14, true, '2025-06-20 10:00:00', 2),

('Startup française révolutionne le marché de la mobilité électrique',
 'Une jeune pousse parisienne innove dans les solutions de transport durable.',
 $$
 Une jeune startup parisienne fait bouger les lignes du secteur de la mobilité électrique grâce à des solutions innovantes, pratiques et respectueuses de l’environnement. Cette entreprise ambitieuse propose des alternatives durables pour rendre les déplacements urbains plus propres et accessibles à tous.

 ## Des solutions adaptées aux besoins des citadins

 Face à l’augmentation de la pollution et à la saturation des transports en commun, cette startup développe des véhicules électriques compacts et modulables, idéaux pour les trajets courts en ville. Leur design ingénieux permet une grande maniabilité et une recharge rapide, facilitant ainsi l’adoption par un large public.

 ## Favoriser une mobilité plus responsable

 Au-delà des véhicules, l’entreprise met en place un réseau intelligent de bornes de recharge alimentées par des énergies renouvelables. Cette infrastructure connectée optimise l’utilisation de l’électricité verte et encourage les utilisateurs à opter pour des modes de déplacement plus respectueux de la planète.

 ## Soutien à l’économie locale et emploi

 Cette startup mise également sur la création d’emplois locaux et le développement de compétences en haute technologie, contribuant ainsi à dynamiser l’écosystème entrepreneurial français tout en participant à la transition énergétique.

 ---

 Avec des idées novatrices et un engagement fort pour un avenir durable, cette jeune pousse parisienne montre la voie vers une mobilité électrique plus accessible, écologique et intelligente. Une belle source d’inspiration pour tous ceux qui souhaitent un changement positif dans leur manière de se déplacer !
 $$,
 'https://images.pexels.com/photos/9800006/pexels-photo-9800006.jpeg', 15, true, '2025-06-18 14:30:00', 4),

('Nouvelles perspectives dans la recherche spatiale',
 'Les dernières missions d’exploration de Mars ouvrent de nouvelles voies scientifiques.',
 $$
 Les missions récentes d’exploration de Mars ouvrent des horizons passionnants pour la science et la compréhension de notre système solaire. Grâce aux technologies avancées et à la collaboration internationale, ces projets repoussent les limites de notre savoir et préparent l’avenir de l’exploration spatiale.

 ## Des découvertes clés sur la planète rouge

 Les rovers et sondes envoyés sur Mars recueillent des données inédites sur la composition du sol, l’atmosphère et les traces d’eau passée. Ces informations permettent aux scientifiques de mieux comprendre l’histoire géologique de la planète et d’évaluer son potentiel à abriter la vie, passée ou présente.

 ## Technologies innovantes pour l’exploration

 Les dernières missions intègrent des innovations comme les drones aériens, les laboratoires mobiles et les systèmes d’intelligence artificielle embarqués. Ces outils autonomes améliorent la capacité d’exploration en terrain difficile et facilitent l’analyse en temps réel des données collectées.

 ## Vers une présence humaine durable

 Les avancées actuelles posent les bases pour une future mission habitée sur Mars. Les recherches sur les habitats, la production d’oxygène et l’utilisation des ressources locales ouvrent la voie à une présence humaine durable sur la planète rouge.

 ---

 Ces nouvelles perspectives dans la recherche spatiale stimulent l’imaginaire collectif et renforcent notre désir d’explorer l’inconnu. Chaque découverte nous rapproche un peu plus de la conquête de nouveaux mondes, avec pour horizon un avenir rempli de promesses et d’espoirs.
 $$,
 'https://images.pexels.com/photos/586072/pexels-photo-586072.jpeg', 16, false, null, 8),

('L’impact positif des jeux vidéo sur le bien-être mental',
 'Une étude récente démontre les bienfaits de certains jeux sur la santé cognitive.',
 $$
 Contrairement aux idées reçues, les jeux vidéo peuvent avoir des effets bénéfiques sur la santé mentale et le bien-être. Une étude récente met en lumière comment certains jeux, bien choisis et pratiqués avec modération, contribuent à améliorer les fonctions cognitives et à réduire le stress.

 ## Amélioration des capacités cognitives

 Les jeux vidéo stimulent la mémoire, la concentration et la capacité à résoudre des problèmes. Certains titres demandent aux joueurs d’élaborer des stratégies complexes, ce qui favorise la pensée critique et la créativité. Cette stimulation régulière peut renforcer les connexions neuronales et améliorer la plasticité cérébrale.

 ## Réduction du stress et de l’anxiété

 Jouer à des jeux vidéo permet aussi de se détendre et d’échapper temporairement aux sources de stress du quotidien. Les expériences immersives et interactives offrent un espace de relaxation et de plaisir, favorisant la libération d’endorphines, les hormones du bien-être.

 ## Renforcement des liens sociaux

 Les jeux en ligne favorisent les interactions sociales, même à distance. Ils permettent de créer des communautés, de partager des expériences et de collaborer, ce qui peut réduire le sentiment d’isolement et renforcer le soutien social.

 ---

 Cette étude rappelle que les jeux vidéo, utilisés de manière équilibrée, sont bien plus qu’un simple divertissement : ils peuvent devenir un véritable allié pour notre santé mentale et notre épanouissement personnel.
 $$,
 'https://images.pexels.com/photos/32693966/pexels-photo-32693966.jpeg', 18, true, '2025-06-22 09:00:00', 10),

('L’entrepreneuriat féminin au cœur de la nouvelle économie',
 'Des femmes inspirantes transforment le monde des affaires avec leurs idées novatrices.',
 $$
 Dans un monde en pleine transformation économique, les femmes entrepreneures jouent un rôle de plus en plus déterminant. Avec leurs idées novatrices et leur engagement, elles façonnent un avenir plus inclusif, durable et dynamique.

 ## Des modèles inspirants

 De nombreuses femmes créent des entreprises qui répondent aux défis contemporains, qu’il s’agisse de technologies propres, d’innovation sociale ou de nouveaux modes de consommation. Ces entrepreneures montrent que créativité et leadership peuvent changer les règles du jeu.

 ## Un soutien grandissant

 Les réseaux, incubateurs et fonds dédiés à l’entrepreneuriat féminin se multiplient, offrant des ressources, du mentorat et des opportunités de financement. Cette dynamique favorise l’émergence de projets porteurs qui contribuent à diversifier l’économie.

 ## Impact social et économique

 Au-delà de la réussite individuelle, l’entrepreneuriat féminin contribue à générer des emplois, à promouvoir l’égalité des chances et à renforcer la résilience des communautés. Ces initiatives participent à construire une économie plus juste et solidaire.

 ---

 Avec leur détermination et leur vision, les femmes entrepreneures sont des actrices clés de la nouvelle économie. Leur parcours inspire et ouvre la voie à un avenir où innovation rime avec diversité et inclusion.
 $$,
 'https://images.pexels.com/photos/7289723/pexels-photo-7289723.jpeg', 22, true, '2025-06-21 16:45:00', 2),

('Innovations technologiques pour une meilleure santé globale',
 'Les nouvelles technologies facilitent la prévention et le suivi médical.',
 $$
 Les avancées technologiques transforment profondément le domaine de la santé, offrant des outils toujours plus performants pour prévenir les maladies et assurer un suivi personnalisé des patients. Ces innovations ouvrent la voie à une médecine plus proactive, accessible et efficace.

 ## Prévention grâce aux objets connectés

 Les montres intelligentes, bracelets de suivi et autres objets connectés permettent de surveiller en temps réel des paramètres vitaux comme le rythme cardiaque, la qualité du sommeil ou le niveau d’activité physique. Ces données encouragent les utilisateurs à adopter des comportements plus sains avant même l’apparition de symptômes.

 ## Suivi médical personnalisé

 Les applications mobiles et plateformes numériques facilitent la communication entre patients et professionnels de santé. Elles permettent un suivi à distance, un ajustement rapide des traitements et une meilleure gestion des pathologies chroniques, tout en réduisant les déplacements inutiles.

 ## Intelligence artificielle au service du diagnostic

 L’IA analyse de grandes quantités de données médicales pour aider au diagnostic précoce et à la personnalisation des soins. Elle optimise les protocoles de traitement et contribue à la recherche de nouveaux médicaments, améliorant ainsi la qualité des prises en charge.

 ---

 Ces innovations technologiques participent à construire un système de santé plus humain, centré sur la prévention et l’accompagnement personnalisé. Elles représentent un véritable espoir pour améliorer la qualité de vie de chacun, aujourd’hui et demain.
 $$,
 'https://images.pexels.com/photos/17485683/pexels-photo-17485683.png', 17, false, null, 4);



INSERT INTO comments (content, user_id, article_id) VALUES
('Super article, très inspirant ! Merci pour ces bonnes nouvelles.', 3, 1),
('L’IA est vraiment fascinante, merci pour ce résumé clair.', 7, 1),
('J’ai appris plein de choses, top !', 9, 1),

('J’adore cette startup, ça donne vraiment de l’espoir pour l’avenir.', 7, 2),

('Très intéressant, hâte de voir les futures découvertes spatiales.', 9, 5),
('Une lecture motivante, merci pour ce focus sur l’entrepreneuriat féminin.', 6, 5),
('Bravo aux femmes entrepreneures, super initiative !', 3, 5),
('Beaucoup d’inspiration dans cet article.', 4, 5);
