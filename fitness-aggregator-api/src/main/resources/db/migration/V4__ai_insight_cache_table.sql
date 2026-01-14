-- Cache table for AI-generated insights to avoid redundant LLM calls
CREATE TABLE ai_insight_cache (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    metric_type VARCHAR(50) NOT NULL,
    cache_date DATE NOT NULL,
    insight TEXT NOT NULL,
    data_hash VARCHAR(64),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- One cached insight per user per metric per day
    CONSTRAINT uq_ai_cache_user_metric_date UNIQUE (user_id, metric_type, cache_date)
);

-- Index for fast lookups
CREATE INDEX idx_ai_cache_user_metric_date ON ai_insight_cache(user_id, metric_type, cache_date);
