ALTER TABLE users
ADD COLUMN search_vector tsvector;

UPDATE users
SET search_vector =
    to_tsvector(
        'english',
        coalesce(first_name, '') || ' ' ||
        coalesce(last_name, '') || ' ' ||
        coalesce(email, '')
    );


CREATE INDEX idx_user_search_vector
ON users
USING GIN(search_vector);

CREATE OR REPLACE FUNCTION user_search_vector_update()
RETURNS trigger AS
$$
BEGIN
    NEW.search_vector :=
        setweight(
            to_tsvector('english', COALESCE(NEW.email, '')),
            'A'
        )
        ||
        setweight(
            to_tsvector('english', COALESCE(NEW.first_name, '')),
            'B'
        )
        ||
        setweight(
            to_tsvector('english', COALESCE(NEW.last_name, '')),
            'B'
        );

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER user_search_vector_trigger
BEFORE INSERT OR UPDATE
ON users
FOR EACH ROW
EXECUTE FUNCTION user_search_vector_update();