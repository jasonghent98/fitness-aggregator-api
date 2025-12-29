-- 1) ACTIVITIES
CREATE TABLE IF NOT EXISTS fitbit_activity_summaries (
  id                            uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  actualize_user_id  UUID        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  fitbit_user_id     VARCHAR(64) NOT NULL,
  log_id             BIGINT      NOT NULL,
  activity_date      DATE        NOT NULL,
  start_time_local   TIME,
  duration_ms        BIGINT,
  calories           INTEGER,
  distance           DOUBLE PRECISION,
  steps              INTEGER,
  activity_name      TEXT,
  tcx_link           TEXT,
  created_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT uq_fitbit_activities UNIQUE (actualize_user_id, log_id)
);
CREATE INDEX IF NOT EXISTS idx_fitbit_activities_user_date
  ON fitbit_activity_summaries(actualize_user_id, activity_date);

-- 2) SLEEP (per-session)
CREATE TABLE IF NOT EXISTS fitbit_sleep_summaries (
  id                            uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  actualize_user_id  UUID        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  fitbit_user_id     VARCHAR(64) NOT NULL,
  log_id             BIGINT      NOT NULL,
  date_of_sleep      DATE        NOT NULL,
  start_time         TIMESTAMPTZ,
  end_time           TIMESTAMPTZ,
  duration_ms        BIGINT,
  efficiency         INTEGER,
  is_main_sleep      BOOLEAN,
  levels_json        JSONB,
  created_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT uq_fitbit_sleep UNIQUE (actualize_user_id, log_id)
);
CREATE INDEX IF NOT EXISTS idx_fitbit_sleep_user_date
  ON fitbit_sleep_summaries(actualize_user_id, date_of_sleep);

-- 3) FOODS (per-entry)
CREATE TABLE IF NOT EXISTS fitbit_food_summaries (
  id                            uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  actualize_user_id  UUID        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  fitbit_user_id     VARCHAR(64) NOT NULL,
  log_id             BIGINT      NOT NULL,
  log_date           DATE        NOT NULL,
  meal_type_id       INTEGER,
  amount             DOUBLE PRECISION,
  unit_name          TEXT,
  unit_type          TEXT,
  food_name          TEXT,
  brand              TEXT,
  serving_size       DOUBLE PRECISION,
  calories           DOUBLE PRECISION,
  carbs              DOUBLE PRECISION,
  fat                DOUBLE PRECISION,
  fiber              DOUBLE PRECISION,
  protein            DOUBLE PRECISION,
  sodium             DOUBLE PRECISION,
  created_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT uq_fitbit_food UNIQUE (actualize_user_id, log_id)
);
CREATE INDEX IF NOT EXISTS idx_fitbit_food_user_date
  ON fitbit_food_summaries(actualize_user_id, log_date);

-- 4) BODY (weight OR body-fat in one table)
CREATE TABLE IF NOT EXISTS fitbit_body_summaries (
  id                            uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  actualize_user_id  UUID        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  fitbit_user_id     VARCHAR(64) NOT NULL,
  log_id             BIGINT      NOT NULL,
  entry_type         VARCHAR(16) NOT NULL,  -- 'WEIGHT' | 'FAT'
  date               DATE        NOT NULL,
  time_local         TIME,
  -- weight fields (for entry_type = WEIGHT)
  weight_value       DOUBLE PRECISION,
  weight_unit        TEXT,                  -- e.g., "kg" or "lb"
  bmi                NUMERIC(5,2),
  -- body fat fields (for entry_type = FAT)
  fat_percent        NUMERIC(5,2),
  source             VARCHAR(32),
  created_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT uq_fitbit_body UNIQUE (actualize_user_id, log_id)
);
CREATE INDEX IF NOT EXISTS idx_fitbit_body_user_date
  ON fitbit_body_summaries(actualize_user_id, date);