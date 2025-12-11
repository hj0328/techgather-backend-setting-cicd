#!/bin/bash

TARGET="$1"

cd /home/ec2-user/deploy
export DOCKER_REPO="${DOCKER_REPO}"

docker-compose pull $TARGET
docker-compose up -d $TARGET
