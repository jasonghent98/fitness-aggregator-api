-- Garmin Daily Summary Table
CREATE TABLE garmin_daily_summaries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    provider_user_id VARCHAR NOT NULL, -- Garmin userId
    user_id UUID NOT NULL CONSTRAINT fk_user REFERENCES users(id),
    summary_id VARCHAR(255) NOT NULL,
    calendar_date DATE NOT NULL,
    active_kilocalories INT,
    bmr_kilocalories INT,
    steps INT,
    distance_in_meters DOUBLE PRECISION,
    duration_in_seconds INT,
    active_time_in_seconds INT,
    steps_goal INT,
    min_heart_rate INT,
    avg_heart_rate INT,
    max_heart_rate INT,
    resting_heart_rate INT,
    avg_stress_level INT,
    max_stress_level INT,
    stress_duration_in_seconds INT,
    low_stress_duration_in_seconds INT,
    medium_stress_duration_in_seconds INT,
    high_stress_duration_in_seconds INT,
    floors_climbed INT,
    moderate_intensity_duration_in_seconds INT,
    vigorous_intensity_duration_in_seconds INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Garmin Sleep Summary Table
CREATE TABLE garmin_sleep_summaries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    provider_user_id VARCHAR NOT NULL, -- Garmin userId
    user_id UUID NOT NULL CONSTRAINT fk_user REFERENCES users(id),
    summary_id VARCHAR(255) NOT NULL,
    calendar_date DATE NOT NULL,
    start_time_in_seconds BIGINT,
    start_time_offset_in_seconds INT,
    duration_in_seconds INT,
    total_nap_duration_in_seconds INT,
    unmeasurable_sleep_in_seconds INT,
    deep_sleep_duration_in_seconds INT,
    light_sleep_duration_in_seconds INT,
    rem_sleep_in_seconds INT,
    awake_duration_in_seconds INT,
    validation VARCHAR(50),
    overall_sleep_score JSONB,
    sleep_scores JSONB,
    naps JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Garmin Stress Summary Table
CREATE TABLE garmin_stress_summaries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    provider_user_id VARCHAR NOT NULL, -- Garmin userId
    user_id UUID NOT NULL CONSTRAINT fk_user REFERENCES users(id),
    summary_id VARCHAR(255) NOT NULL,
    calendar_date DATE NOT NULL,
    start_time_in_seconds BIGINT,
    start_time_offset_in_seconds INT,
    duration_in_seconds INT,
    avg_stress_level INT,
    max_stress_level INT,
    stress_details JSONB,
    body_battery_events JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Garmin HRV Summary Table
CREATE TABLE garmin_hrv_summaries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    provider_user_id VARCHAR NOT NULL, -- Garmin userId
    user_id UUID NOT NULL CONSTRAINT fk_user REFERENCES users(id),
    summary_id VARCHAR(255) NOT NULL,
    calendar_date DATE NOT NULL,
    start_time_in_seconds BIGINT,
    start_time_offset_in_seconds INT,
    duration_in_seconds INT,
    avg_rmssd DOUBLE PRECISION,
    avg_sdrr DOUBLE PRECISION,
    hrv_details JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Garmin Pulse Ox Summary Table
CREATE TABLE garmin_pulseox_summaries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    provider_user_id VARCHAR NOT NULL, -- Garmin userId
    user_id UUID NOT NULL CONSTRAINT fk_user REFERENCES users(id),
    summary_id VARCHAR(255) NOT NULL,
    calendar_date DATE NOT NULL,
    start_time_in_seconds BIGINT,
    start_time_offset_in_seconds INT,
    duration_in_seconds INT,
    avg_spo2 DOUBLE PRECISION,
    min_spo2 INT,
    max_spo2 INT,
    spo2_samples JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);

-- Indexes for faster querying by user/date
CREATE INDEX idx_garmin_daily_user_date ON garmin_daily_summaries(user_id, calendar_date);
CREATE INDEX idx_garmin_sleep_user_date ON garmin_sleep_summaries(user_id, calendar_date);
CREATE INDEX idx_garmin_stress_user_date ON garmin_stress_summaries(user_id, calendar_date);
CREATE INDEX idx_garmin_hrv_user_date   ON garmin_hrv_summaries(user_id, calendar_date);
CREATE INDEX idx_garmin_pulseox_user_date ON garmin_pulseox_summaries(user_id, calendar_date);