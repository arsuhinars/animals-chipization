CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX accs_first_name_idx ON accounts USING GIN (LOWER(first_name) gin_trgm_ops);
CREATE INDEX accs_last_name_idx ON accounts USING GIN (LOWER(last_name) gin_trgm_ops);
CREATE INDEX accs_email_idx ON accounts USING GIN (LOWER(email) gin_trgm_ops);
