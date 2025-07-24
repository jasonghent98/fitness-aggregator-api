#!/bin/bash

# Get the absolute path to the project root (1 level up from this script)
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

# Reference all config files from the root
ENV_FILE="$PROJECT_ROOT/.env.dev"
COMPOSE_FILE_BASE="$PROJECT_ROOT/docker-compose.yml"
COMPOSE_FILE_ENV="$PROJECT_ROOT/docker-compose.dev.yml"

echo "🟢 Starting Docker containers for DEV..."
echo "Using env file:         $ENV_FILE"
echo "Using compose files:    $COMPOSE_FILE_BASE + $COMPOSE_FILE_ENV"

docker-compose \
  --env-file "$ENV_FILE" \
  -f "$COMPOSE_FILE_BASE" \
  -f "$COMPOSE_FILE_ENV" \
  up --build