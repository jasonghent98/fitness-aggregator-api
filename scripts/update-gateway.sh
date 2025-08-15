#!/bin/bash
set -e

# === SETTINGS ===
ENV="dev"
PROJECT="actualize-dev"
API_NAME="dev-api"
GATEWAY_NAME="dev-gateway"
LOCATION="us-central1"
OPENAPI_SPEC="fitness-aggregator-api/gateway/dev/config.yaml"
SERVICE_ACCOUNT="api-invoker-dev@${PROJECT}.iam.gserviceaccount.com"

# === SCRIPT ===
CFG_NAME="${ENV}-api-config-$(date +%Y%m%d-%H%M%S)"

echo "📦 Creating API config: $CFG_NAME"
gcloud api-gateway api-configs create "$CFG_NAME" \
  --api="$API_NAME" \
  --openapi-spec="$OPENAPI_SPEC" \
  --project="$PROJECT" \
  --backend-auth-service-account="$SERVICE_ACCOUNT"

echo "🚀 Updating gateway: $GATEWAY_NAME to use config: $CFG_NAME"
gcloud api-gateway gateways update "$GATEWAY_NAME" \
  --api="$API_NAME" \
  --api-config="$CFG_NAME" \
  --location="$LOCATION" \
  --project="$PROJECT"

echo "✅ Done! Gateway now uses $CFG_NAME"