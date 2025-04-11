CREATE TABLE IF NOT EXISTS journal_entries (
    id SERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    date TIMESTAMP NOT NULL,
    user_id INT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS moods (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS journal_entries_moods (
    id SERIAL PRIMARY KEY,
    journal_entry_id INT NOT NULL,
    mood_id INT NOT NULL,
    CONSTRAINT fk_journal_entry FOREIGN KEY (journal_entry_id) REFERENCES journal_entries(id) ON DELETE CASCADE,
    CONSTRAINT fk_mood FOREIGN KEY (mood_id) REFERENCES moods(id) ON DELETE CASCADE
);