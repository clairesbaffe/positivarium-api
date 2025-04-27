CREATE OR REPLACE FUNCTION set_created_at_and_published_at_column()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.created_at IS NULL THEN
        NEW.created_at = CURRENT_TIMESTAMP;
        NEW.updated_at = CURRENT_TIMESTAMP;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION set_created_at_column()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.created_at IS NULL THEN
        NEW.created_at = CURRENT_TIMESTAMP;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION set_published_at_column()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.is_published = TRUE AND OLD.is_published = FALSE THEN
        IF NEW.published_at IS NULL THEN
            NEW.published_at = CURRENT_TIMESTAMP;
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_created_at_and_published_at_articles
BEFORE INSERT ON articles
FOR EACH ROW
EXECUTE FUNCTION set_created_at_and_published_at_column();

CREATE TRIGGER set_updated_at_articles
BEFORE UPDATE ON articles
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER set_published_at_articles
BEFORE UPDATE ON articles
FOR EACH ROW
EXECUTE FUNCTION set_published_at_column();

CREATE TRIGGER set_created_at_comments
BEFORE INSERT ON comments
FOR EACH ROW
EXECUTE FUNCTION set_created_at_column();
