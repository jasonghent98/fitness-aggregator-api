#!/bin/bash

# Load environment variables from .env.dev
export SPRING_PROFILES_ACTIVE=production
export $(grep -v '^#' ./fitness-aggregator-api/.env.test | xargs)

# Run the Maven build
./mvnw clean install -U