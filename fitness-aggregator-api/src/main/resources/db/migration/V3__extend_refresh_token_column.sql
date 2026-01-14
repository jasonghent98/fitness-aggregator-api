-- Extend refresh_token column to accommodate JWT tokens (which can be 300-500+ chars)
ALTER TABLE user_sessions ALTER COLUMN refresh_token TYPE VARCHAR(512);
