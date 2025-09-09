-- V3__create_email_verifications.sql

-- Ensure citext is available (only runs if not already installed)
CREATE EXTENSION IF NOT EXISTS citext;

CREATE TABLE IF NOT EXISTS email_verifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email CITEXT NOT NULL UNIQUE,
    access_token TEXT NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Index for faster lookups
CREATE INDEX IF NOT EXISTS idx_email_verifications_email
    ON email_verifications (email);
CREATE INDEX IF NOT EXISTS idx_email_verifications_access_token
    ON email_verifications (access_token);