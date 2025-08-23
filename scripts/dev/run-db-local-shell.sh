#!/bin/bash
# for testing purposes: runs the interactive docker container for psql (DEV)
docker exec -it fitness-aggregator-api-postgres-1 psql -U actuserdev -d actualizedev