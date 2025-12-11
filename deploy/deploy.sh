#!/bin/bash

TARGET="$1"

cd /home/ec2-user/tech-gather
export DOCKER_REPO="${DOCKER_REPO}"

echo "Pulling image.. "
docker-compose pull $TARGET

echo "Restarting service..."
docker-compose up -d $TARGET

CONFIG="/home/ec2-user/tech-gather/services.json"
PORT=$(jq -r ".\"$TARGET\".port" $CONFIG)
HEALTH_PATH=$(jq -r ".\"$TARGET\".health" $CONFIG)

HEALTH_URL="http://localhost:${PORT}${HEALTH_PATH}"

for i in {1..20}; do
  STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$HEALTH_URL")
  if [[ "$STATUS" == "200" ]]; then
    echo "Health check OK!"
    exit 0
  fi
  echo "Health check failed ($STATUS)… retry $i/20"
  sleep 3
done

echo "Deployment completed"
