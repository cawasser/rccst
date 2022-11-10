GRANT ALL PRIVILEGES ON DATABASE rccst TO "1001710000";

-- Remove if exists
DROP TABLE IF EXISTS users;

-- Create Users table
CREATE TABLE IF NOT EXISTS users (
    username        TEXT PRIMARY KEY,
    uuid            TEXT,
    pass            TEXT);


-- Add default user
INSERT INTO users (username, uuid, pass)
VALUES ('test-user', 'test-user-uuid', 'test-pwd')