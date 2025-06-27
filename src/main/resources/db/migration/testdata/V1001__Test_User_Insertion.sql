INSERT INTO users (username, password, enabled) VALUES
('Claire Sbaffe', '$2a$10$yJrK77Tkvibxcx2SvL5HFOqUbRbGG14SMJcL8jpCHCqafmGavFghi', true),    -- ADMIN PUBLISHER USER
('Paul Martin', '$2a$10$yJrK77Tkvibxcx2SvL5HFOqUbRbGG14SMJcL8jpCHCqafmGavFghi', true),      -- PUBLISHER
('Emma Leroy', '$2a$10$yJrK77Tkvibxcx2SvL5HFOqUbRbGG14SMJcL8jpCHCqafmGavFghi', true),       -- USER
('Alex Johnson', '$2a$10$yJrK77Tkvibxcx2SvL5HFOqUbRbGG14SMJcL8jpCHCqafmGavFghi', true),     -- PUBLISHER
('Sophie Blanc', '$2a$10$yJrK77Tkvibxcx2SvL5HFOqUbRbGG14SMJcL8jpCHCqafmGavFghi', true),     -- USER BAN
('Mia Smith', '$2a$10$yJrK77Tkvibxcx2SvL5HFOqUbRbGG14SMJcL8jpCHCqafmGavFghi', true),        -- ADMIN
('Olivia Martinez', '$2a$10$yJrK77Tkvibxcx2SvL5HFOqUbRbGG14SMJcL8jpCHCqafmGavFghi', true),  -- USER
('Emily Davis', '$2a$10$yJrK77Tkvibxcx2SvL5HFOqUbRbGG14SMJcL8jpCHCqafmGavFghi', true),      -- PUBLISHER BAN
('Manon Fabre', '$2a$10$yJrK77Tkvibxcx2SvL5HFOqUbRbGG14SMJcL8jpCHCqafmGavFghi', true),      -- USER
('Thomas Garcia', '$2a$10$yJrK77Tkvibxcx2SvL5HFOqUbRbGG14SMJcL8jpCHCqafmGavFghi', true);    -- PUBLISHER

INSERT INTO users_roles (users_id, roles_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 2),
(3, 3),
(4, 2),
(5, 3),
(5, 4),
(6, 1),
(7, 3),
(8, 2),
(8, 4),
(9, 3),
(10, 2);