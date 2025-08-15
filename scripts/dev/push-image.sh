#!/usr/bin/env bash
set -euo pipefail

root="$(cd "$(dirname "$0")/../.." && pwd)"

ENV="dev"
PROJECT="actualize-dev"
APP_DIR="${root}/fitness-aggregator-api"

REG_HOST="us-central1-docker.pkg.dev"
REG_REPO="backend-repo"
IMAGE_NAME="actualize-api"
REG_PATH="${REG_HOST}/${PROJECT}/${REG_REPO}/${IMAGE_NAME}"

TAG_FILE="${APP_DIR}/.last_image_tag.${ENV}"
[[ -f "${TAG_FILE}" ]] || { echo "❌ ${TAG_FILE} not found. Run scripts/${ENV}/build-image.sh first."; exit 1; }
TAG="$(cat "${TAG_FILE}")"

echo "🔑 Configuring docker auth for ${REG_HOST}..."
gcloud auth configure-docker "${REG_HOST}" -q

echo "📤 Pushing:"
echo "  ${REG_PATH}:${TAG}"
echo "  ${REG_PATH}:latest-${ENV}"
docker push "${REG_PATH}:${TAG}"
docker push "${REG_PATH}:latest-${ENV}"
echo "✅ Done."