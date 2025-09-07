CREATE TABLE IF NOT EXISTS leads (
  id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email              CITEXT NOT NULL UNIQUE,
  full_name          TEXT,
  source             TEXT NOT NULL,
  status             TEXT NOT NULL DEFAULT 'pending',
  survey_answers     JSONB,
  consent_marketing  BOOLEAN NOT NULL DEFAULT FALSE,
  ip                 TEXT,
  user_agent         TEXT,
  created_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at         TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_leads_status      ON leads (status);
CREATE INDEX IF NOT EXISTS idx_leads_created_at  ON leads (created_at);