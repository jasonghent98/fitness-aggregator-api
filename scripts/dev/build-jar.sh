#!/bin/bash

# Resolve project root from this script's location: scripts/dev/build-jar.sh -> repo root
SCRIPT_DIR="$(cd -- "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
API_DIR="$PROJECT_ROOT/fitness-aggregator-api"
ENV_FILE="$API_DIR/.env.dev"

echo "PROJECT_ROOT=$PROJECT_ROOT"
echo "API_DIR=$API_DIR"
echo "ENV_FILE=$ENV_FILE"

# Load .env.dev safely (supports quotes, # comments, spaces)
if [[ -f "$ENV_FILE" ]]; then
  set -a            # auto-export all variables
  # shellcheck disable=SC1090
  source "$ENV_FILE"
  set +a
else
  echo "⚠️  Env file not found: $ENV_FILE"
fi

# Default Spring profile if not provided in .env.dev
export SPRING_PROFILES_ACTIVE="${SPRING_PROFILES_ACTIVE:-dev}"

# Build
cd "$PROJECT_ROOT"
./mvnw clean install -U