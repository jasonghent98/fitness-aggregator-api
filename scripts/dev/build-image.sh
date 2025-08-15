#!/usr/bin/env bash
set -euo pipefail

root="$(cd "$(dirname "$0")/../.." && pwd)"

ENV="dev"
PROJECT="actualize-dev"
APP_DIR="${root}/fitness-aggregator-api"
DOCKERFILE="${APP_DIR}/Dockerfile"

REG_HOST="us-central1-docker.pkg.dev"
REG_REPO="backend-repo"
IMAGE_NAME="actualize-api"
REG_PATH="${REG_HOST}/${PROJECT}/${REG_REPO}/${IMAGE_NAME}"

TAG="${ENV}-$(date +%Y%m%d-%H%M%S)"

echo "🚀 Building image ${REG_PATH}:${TAG}"
docker buildx build --platform linux/amd64 \
  -f "${DOCKERFILE}" "${APP_DIR}" \
  -t "${REG_PATH}:${TAG}" \
  -t "${REG_PATH}:latest-${ENV}"

echo "${TAG}" > "${APP_DIR}/.last_image_tag.${ENV}"
echo "📝 Wrote tag to ${APP_DIR}/.last_image_tag.${ENV}"