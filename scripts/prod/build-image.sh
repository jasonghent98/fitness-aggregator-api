#!/bin/bash

root="$(cd "$(dirname "$0")/../.." && pwd)"
source "${root}/scripts/common/config.sh"

ENV="prod"
PROJECT="actualize-prod"       # <-- set your prod project id

REG_PATH="$(reg_path "${PROJECT}")"
TAG="${ENV}-$(date +%Y%m%d-%H%M%S)"

echo "🚀 Building image ${REG_PATH}:${TAG}"
docker buildx build --platform linux/amd64 \
  -f "${DOCKERFILE}" "${APP_DIR}" \
  -t "${REG_PATH}:${TAG}" \
  -t "${REG_PATH}:latest-${ENV}"

echo "${TAG}" > "${APP_DIR}/.last_image_tag.${ENV}"
echo "📝 Wrote tag to ${APP_DIR}/.last_image_tag.${ENV}"