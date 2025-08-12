#!/bin/bash

# Absolute path to project root
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
echo "PROJECT ROOT => $PROJECT_ROOT"
# Reference all config files from inside API module
ENV_FILE="$PROJECT_ROOT/../fitness-aggregator-api/.env.dev"
COMPOSE_FILE_BASE="$PROJECT_ROOT/../fitness-aggregator-api/docker-compose.yml"
COMPOSE_FILE_ENV="$PROJECT_ROOT/../fitness-aggregator-api/docker-compose.dev.yml"

echo "🟢 Starting Docker containers for DEV..."
echo "Using env file:       $ENV_FILE"
echo "Using compose files:  $COMPOSE_FILE_BASE + $COMPOSE_FILE_ENV"

docker-compose \
  --env-file "$ENV_FILE" \
  -f "$COMPOSE_FILE_BASE" \
  -f "$COMPOSE_FILE_ENV" \
  up --build