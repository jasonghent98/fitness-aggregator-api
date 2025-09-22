-- Garmin activities table
CREATE TABLE IF NOT EXISTS garmin_activities (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Link to user
    provider_user_id VARCHAR NOT NULL, -- Garmin userId
    user_id UUID NOT NULL CONSTRAINT fk_user REFERENCES users(id),

    -- Garmin-specific identifiers
    summary_id TEXT,
    activity_id BIGINT NOT NULL,
    calendar_date DATE NOT NULL,

    -- Activity metadata
    activity_name TEXT,
    activity_description TEXT,
    is_parent BOOLEAN,
    parent_summary_id TEXT,

    -- Timing
    duration_in_seconds INT,
    start_time_in_seconds BIGINT,
    start_time_offset_in_seconds INT,
    number_of_active_lengths INT,

    -- Type + device
    activity_type TEXT,
    device_name TEXT,

    -- Core metrics
    distance_in_meters DOUBLE PRECISION,
    steps INT,
    pushes INT,
    active_kilocalories INT,

    -- Heart rate & cadence
    average_heart_rate_in_beats_per_minute INT,
    max_heart_rate_in_beats_per_minute INT,
    average_run_cadence_in_steps_per_minute INT,
    max_run_cadence_in_steps_per_minute INT,
    average_bike_cadence_in_rounds_per_minute INT,
    max_bike_cadence_in_rounds_per_minute INT,
    average_push_cadence_in_pushes_per_minute INT,
    max_push_cadence_in_pushes_per_minute INT,
    average_swim_cadence_in_strokes_per_minute INT,

    -- Pace / speed
    average_speed_in_meters_per_second DOUBLE PRECISION,
    max_speed_in_meters_per_second DOUBLE PRECISION,
    average_pace_in_minutes_per_kilometer DOUBLE PRECISION,
    max_pace_in_minutes_per_kilometer DOUBLE PRECISION,

    -- Elevation
    total_elevation_gain_in_meters DOUBLE PRECISION,
    total_elevation_loss_in_meters DOUBLE PRECISION,

    -- Location
    starting_latitude_in_degree DOUBLE PRECISION,
    starting_longitude_in_degree DOUBLE PRECISION,

    -- Flags
    manual BOOLEAN,
    is_web_upload BOOLEAN,

    -- Indexes for performance
    CONSTRAINT uq_activity UNIQUE (user_id, activity_id)
);

-- Helpful indexes
CREATE INDEX IF NOT EXISTS idx_garmin_activities_user_date
    ON garmin_activities (user_id, start_time_in_seconds);