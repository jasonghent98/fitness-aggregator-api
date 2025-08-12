#!/bin/bash

# set args
APP_DIR="fitness-aggregator-api"
DOCKERFILE="${APP_DIR}/Dockerfile"
REGION="us-central1"
PROJECT="actualize-dev"
REPO="backend-repo"
IMAGE="actualize-api"
TAG="dev-$(date +%Y%m%d-%H%M%S)"
REG_PATH="${REGION}-docker.pkg.dev/${PROJECT}/${REPO}/${IMAGE}"

# !! assumes JAR already built by build-jar.sh !!
docker buildx build --platform linux/amd64 \
  -f "${DOCKERFILE}"