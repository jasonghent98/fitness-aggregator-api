#!/usr/bin/env bash
set -euo pipefail

root="$(cd "$(dirname "$0")/../.." && pwd)"

ENV="dev"
PROJECT="actualize-dev"
REGION="us-central1"

SERVICE="dev-actualize-api"
RUNTIME_SA="actualize-dev-api@${PROJECT}.iam.gserviceaccount.com"
SQL_INSTANCE="actualize-dev-db-lite"

APP_DIR="${root}/fitness-aggregator-api"
ENV_FILE="${APP_DIR}/env.${ENV}.yaml"
[[ -f "${ENV_FILE}" ]] || { echo "❌ Env file not found: ${ENV_FILE}"; exit 1; }

REG_HOST="us-central1-docker.pkg.dev"
REG_REPO="backend-repo"
IMAGE_NAME="actualize-api"
REG_PATH="${REG_HOST}/${PROJECT}/${REG_REPO}/${IMAGE_NAME}"

TAG_FILE="${APP_DIR}/.last_image_tag.${ENV}"
[[ -f "${TAG_FILE}" ]] || { echo "❌ ${TAG_FILE} not found. Run scripts/${ENV}/build-image.sh first."; exit 1; }
TAG="$(cat "${TAG_FILE}")"
IMAGE="${REG_PATH}:${TAG}"

CONN="$(gcloud sql instances describe "${SQL_INSTANCE}" --project="${PROJECT}" --format='value(connectionName)')" || CONN=""

echo "🚀 Deploying ${SERVICE}"
echo "  Project:   ${PROJECT}"
echo "  Region:    ${REGION}"
echo "  Image:     ${IMAGE}"
echo "  Env file:  ${ENV_FILE}"
[[ -n "${CONN}" ]] && echo "  SQL Conn:  ${CONN}" || echo "  SQL Conn:  (none)"

DEPLOY_CMD=( gcloud run deploy "${SERVICE}"
  --project "${PROJECT}"
  --region "${REGION}"
  --image "${IMAGE}"
  --service-account "${RUNTIME_SA}"
  --env-vars-file "${ENV_FILE}"
  --platform managed
)
[[ -n "${CONN}" ]] && DEPLOY_CMD+=( --add-cloudsql-instances "${CONN}" )

echo "👉 Running: ${DEPLOY_CMD[*]}"
"${DEPLOY_CMD[@]}"

gcloud run services describe "${SERVICE}" --region "${REGION}" --project "${PROJECT}" --format='value(status.url)'