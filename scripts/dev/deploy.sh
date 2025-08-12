#!/bin/bash

root="$(cd "$(dirname "$0")/../.." && pwd)"
source "${root}/scripts/common/config.sh"

ENV="dev"
PROJECT="actualize-dev"
SERVICE="dev-actualize-api"
RUNTIME_SA="actualize-dev-api@${PROJECT}.iam.gserviceaccount.com"
SQL_INSTANCE="actualize-dev-db"
ENV_FILE="${APP_DIR}/env.dev.yaml"          # keep this out of git

TAG_FILE="${APP_DIR}/.last_image_tag.${ENV}"
[[ -f "${TAG_FILE}" ]] || { echo "Missing ${TAG_FILE}. Run scripts/${ENV}/build-image.sh"; exit 1; }

TAG="$(cat "${TAG_FILE}")"
REG_PATH="$(reg_path "${PROJECT}")"
IMAGE="${REG_PATH}:${TAG}"
CONN="$(gcloud sql instances describe "${SQL_INSTANCE}" --project="${PROJECT}" --format='value(connectionName)')"

echo "Deploying ${SERVICE}"
echo "  Project:   ${PROJECT}"
echo "  Image:     ${IMAGE}"
echo "  SQL Conn:  ${CONN}"
echo "  Env file:  ${ENV_FILE}"

gcloud run deploy "${SERVICE}" \
  --project "${PROJECT}" \
  --region "${REGION}" \
  --image "${IMAGE}" \
  --service-account "${RUNTIME_SA}" \
  --add-cloudsql-instances "${CONN}" \
  --env-vars-file "${ENV_FILE}" \
  --platform managed

gcloud run services describe "${SERVICE}" --region "${REGION}" --format='value(status.url)'