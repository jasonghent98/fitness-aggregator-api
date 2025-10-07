-- Add the stripe customer id column to users table
ALTER TABLE users
    ADD COLUMN IF NOT EXISTS stripe_customer_id TEXT DEFAULT NULL;

-- ensure uniqueness
ALTER TABLE users
    ADD CONSTRAINT users_stripe_customer_id_uk UNIQUE (stripe_customer_id);