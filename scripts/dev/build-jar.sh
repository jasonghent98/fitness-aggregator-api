#!/bin/bash

# Load environment variables from .env.dev
export SPRING_PROFILES_ACTIVE=dev
export $(grep -v '^#' ./fitness-aggregator-api/.env.dev | xargs)

# Run the Maven build
./mvnw clean install -U