#!/bin/bash
# start it
docker start actualize-db-dev
# for testing purposes: runs the interactive docker container for psql (DEV)
docker exec -it actualize-db-dev psql -U actuserdev -d actualizedev