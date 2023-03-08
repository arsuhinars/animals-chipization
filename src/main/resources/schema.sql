CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX accs_first_name_idx ON accounts USING GIST (first_name gist_trgm_ops);
CREATE INDEX accs_last_name_idx ON accounts USING GIST (last_name gist_trgm_ops);
CREATE INDEX accs_email_idx ON accounts USING GIST (email gist_trgm_ops);
