#!/bin/bash

root="$(cd "$(dirname "$0")/../.." && pwd)"
source "${root}/scripts/common/config.sh"

ENV="prod"
PROJECT="actualize-prod"       # <-- set your prod project id

REG_PATH="$(reg_path "${PROJECT}")"
TAG_FILE="${APP_DIR}/.last_image_tag.${ENV}"
if [[ ! -f "${TAG_FILE}" ]]; then
  echo "❌ ${TAG_FILE} not found. Run scripts/${ENV}/build-image.sh first."
  exit 1
fi
TAG="$(cat "${TAG_FILE}")"

gcloud auth configure-docker "${REG_HOST}" -q
docker push "${REG_PATH}:${TAG}"
docker push "${REG_PATH}:latest-${ENV}"
echo "✅ Pushed ${ENV} images."