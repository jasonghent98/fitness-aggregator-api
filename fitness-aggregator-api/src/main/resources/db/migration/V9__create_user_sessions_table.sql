-- Create user_sessions table
CREATE TABLE IF NOT EXISTS user_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,

    refresh_token VARCHAR(255) NOT NULL UNIQUE,
    refresh_token_expires_at TIMESTAMPTZ NOT NULL,

    revoked_at TIMESTAMPTZ NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Index for fast lookup by user
CREATE INDEX IF NOT EXISTS idx_user_sessions_user_id
    ON user_sessions (user_id);

-- Index for expiring old tokens quickly
CREATE INDEX IF NOT EXISTS idx_user_sessions_expiry
    ON user_sessions (refresh_token_expires_at);

-- Index for checking revoked sessions
CREATE INDEX IF NOT EXISTS idx_user_sessions_revoked
    ON user_sessions (revoked_at);
