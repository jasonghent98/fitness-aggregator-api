#!/bin/bash

root="$(cd "$(dirname "$0")/../.." && pwd)"
source "${root}/scripts/common/config.sh"

ENV="prod"
PROJECT="actualize-prod"                 # <-- your prod project id
SERVICE="actualize-api"
RUNTIME_SA="actualize-prod-api@${PROJECT}.iam.gserviceaccount.com"
SQL_INSTANCE="actualize-prod-db"
ENV_FILE="${APP_DIR}/env.prod.yaml"

TAG_FILE="${APP_DIR}/.last_image_tag.${ENV}"
[[ -f "${TAG_FILE}" ]] || { echo "Missing ${TAG_FILE}. Run scripts/${ENV}/build-image.sh"; exit 1; }

TAG="$(cat "${TAG_FILE}")"
REG_PATH="$(reg_path "${PROJECT}")"
IMAGE="${REG_PATH}:${TAG}"
CONN="$(gcloud sql instances describe "${SQL_INSTANCE}" --project="${PROJECT}" --format='value(connectionName)')"

echo "Deploying ${SERVICE} (${ENV})"

gcloud run deploy "${SERVICE}" \
  --project "${PROJECT}" \
  --region "${REGION}" \
  --image "${IMAGE}" \
  --service-account "${RUNTIME_SA}" \
  --add-cloudsql-instances "${CONN}" \
  --env-vars-file "${ENV_FILE}" \
  --platform managed

gcloud run services describe "${SERVICE}" --region "${REGION}" --format='value(status.url)'