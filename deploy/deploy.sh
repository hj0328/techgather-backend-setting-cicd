#!/bin/bash

TARGET="$1"

cd /home/ec2-user/tech-gather
export DOCKER_REPO="${DOCKER_REPO}"

echo "Pulling image.. "
docker-compose pull $TARGET

echo "Restarting service..."
docker-compose up -d $TARGET

echo "Deployment completed"
