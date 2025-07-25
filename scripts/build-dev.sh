#!/bin/bash

# Load environment variables from .env.dev
export SPRING_PROFILES_ACTIVE=dev
export $(grep -v '^#' .env.dev | xargs)

# Run the Maven build
./mvnw clean install -U