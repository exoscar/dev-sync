ALTER TABLE issue
ADD COLUMN search_vector tsvector;

UPDATE issue
SET search_vector =
    to_tsvector(
        'english',
        coalesce(title, '') || ' ' ||
        coalesce(description, '')
    );

CREATE INDEX idx_issue_search
ON issue USING GIN(search_vector);

CREATE OR REPLACE FUNCTION issue_search_vector_update()
RETURNS trigger AS
$$
BEGIN
    NEW.search_vector :=
        setweight(
            to_tsvector('english', COALESCE(NEW.title, '')),
            'A'
        )
        ||
        setweight(
            to_tsvector('english', COALESCE(NEW.description, '')),
            'B'
        );

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER issue_search_vector_trigger
BEFORE INSERT OR UPDATE
ON issue
FOR EACH ROW
EXECUTE FUNCTION issue_search_vector_update();