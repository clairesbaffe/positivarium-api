CREATE TABLE IF NOT EXISTS daily_news_preferences (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id INT NOT NULL,
    journal_entry_id INT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_journal_entry FOREIGN KEY (journal_entry_id) REFERENCES journal_entries(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS daily_news_preferences_categories (
    id SERIAL PRIMARY KEY,
    category_id INT NOT NULL,
    daily_news_preference_id INT NOT NULL,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES news_categories(id) ON DELETE CASCADE,
    CONSTRAINT fk_daily_news_preference FOREIGN KEY (daily_news_preference_id) REFERENCES daily_news_preferences(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS global_news_preferences (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    mood_id INT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_mood FOREIGN KEY (mood_id) REFERENCES moods(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS global_news_preferences_categories (
    id SERIAL PRIMARY KEY,
    category_id INT NOT NULL,
    global_news_preference_id INT,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES news_categories(id) ON DELETE CASCADE,
    CONSTRAINT fk_global_news_preference FOREIGN KEY (global_news_preference_id) REFERENCES global_news_preferences(id) ON DELETE CASCADE
);