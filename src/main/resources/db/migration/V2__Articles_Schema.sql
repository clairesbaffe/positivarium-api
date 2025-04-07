CREATE TABLE IF NOT EXISTS articles (
    id SERIAL PRIMARY KEY,
    title VARCHAR(50),
    content TEXT,
    image VARCHAR(255),
    is_published BOOLEAN,
    published_at TIMESTAMP,
    user_id INT,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes (
    id SERIAL PRIMARY KEY,
    user_id INT,
    article_id INT,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_article FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
    id SERIAL PRIMARY KEY,
    content TEXT,
    user_id INT,
    article_id INT,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_article FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS article_reports (
    id SERIAL PRIMARY KEY,
    reason TEXT,
    is_reviewed BOOLEAN,
    article_id INT,
    CONSTRAINT fk_article FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comment_reports (
    id SERIAL PRIMARY KEY,
    reason TEXT,
    is_reviewed BOOLEAN,
    comment_id INT,
    CONSTRAINT fk_comment FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS news_categories (
    id SERIAL PRIMARY KEY,
    category VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS news_tags (
    id SERIAL PRIMARY KEY,
    category VARCHAR(50),
    category_id INT,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES news_categories(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS articles_news_tags (
    id SERIAL PRIMARY KEY,
    article_id INT,
    news_tag_id INT,
    CONSTRAINT fk_article FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE,
    CONSTRAINT fk_news_tag FOREIGN KEY (news_tag_id) REFERENCES news_tags(id) ON DELETE CASCADE
);