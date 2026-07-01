ALTER TABLE project
ADD COLUMN search_vector tsvector;

UPDATE project
SET search_vector =
    to_tsvector(
        'english',
        coalesce(name, '') || ' ' ||
        coalesce(description, '')
    );

CREATE INDEX idx_project_search_vector
ON project
USING GIN(search_vector);

CREATE OR REPLACE FUNCTION project_search_vector_update()
RETURNS trigger AS
$$
BEGIN
    NEW.search_vector :=
        setweight(
            to_tsvector('english', COALESCE(NEW.name, '')),
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


CREATE OR REPLACE TRIGGER project_search_vector_trigger
BEFORE INSERT OR UPDATE
ON project
FOR EACH ROW
EXECUTE FUNCTION project_search_vector_update();
