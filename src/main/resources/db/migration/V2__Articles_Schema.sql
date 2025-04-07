CREATE TABLE IF NOT EXISTS articles (
    id SERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    main_image VARCHAR(255) NOT NULL,
    is_published BOOLEAN NOT NULL,
    published_at TIMESTAMP,
    user_id INT,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS likes (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    article_id INT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_article FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
    id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    user_id INT,
    article_id INT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_article FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS article_reports (
    id SERIAL PRIMARY KEY,
    reason TEXT NOT NULL,
    is_reviewed BOOLEAN NOT NULL,
    article_id INT NOT NULL,
    CONSTRAINT fk_article FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comment_reports (
    id SERIAL PRIMARY KEY,
    reason TEXT NOT NULL,
    is_reviewed BOOLEAN NOT NULL,
    comment_id INT NOT NULL,
    CONSTRAINT fk_comment FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS news_categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS news_tags (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    category_id INT,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES news_categories(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS articles_news_tags (
    id SERIAL PRIMARY KEY,
    article_id INT NOT NULL,
    news_tag_id INT NOT NULL,
    CONSTRAINT fk_article FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE,
    CONSTRAINT fk_news_tag FOREIGN KEY (news_tag_id) REFERENCES news_tags(id) ON DELETE CASCADE
);