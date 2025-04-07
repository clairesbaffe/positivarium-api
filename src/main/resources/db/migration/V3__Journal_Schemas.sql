CREATE TABLE IF NOT EXISTS journal_entries (
    id SERIAL PRIMARY KEY,
    description TEXT,
    date TIMESTAMP,
    user_id INT,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS mood_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    colour VARCHAR(7),
    description TEXT
);

CREATE TABLE IF NOT EXISTS moods (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    type_id INT,
    CONSTRAINT fk_mood_type FOREIGN KEY (type_id) REFERENCES mood_types(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS journal_entries_moods (
    id SERIAL PRIMARY KEY,
    journal_entry_id INT,
    mood_id INT,
    CONSTRAINT fk_journal_entry FOREIGN KEY (journal_entry_id) REFERENCES journal_entries(id) ON DELETE CASCADE,
    CONSTRAINT fk_mood FOREIGN KEY (mood_id) REFERENCES moods(id) ON DELETE CASCADE
);