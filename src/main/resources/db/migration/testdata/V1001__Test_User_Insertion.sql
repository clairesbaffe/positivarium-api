INSERT INTO users (username, email, password, enabled) VALUES
('admin', 'admin@positivarium.com', '$2a$10$yJrK77Tkvibxcx2SvL5HFOqUbRbGG14SMJcL8jpCHCqafmGavFghi', true),
('claire', 'lepositivarium@gmail.com', '$2a$10$yJrK77Tkvibxcx2SvL5HFOqUbRbGG14SMJcL8jpCHCqafmGavFghi',  true);

INSERT INTO users_roles (users_id, roles_id) VALUES
(1, 1),
(2, 1),
(2, 2);