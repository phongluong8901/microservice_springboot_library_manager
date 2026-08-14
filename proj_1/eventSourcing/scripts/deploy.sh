#!/usr/bin/env bash

# Sử dụng biến DOCKER_USERNAME
echo "Deploying with Docker username: $DOCKER_USERNAME"

BRANCH=$(git rev-parse --abbrev-ref HEAD)

echo "Deploying with branch: $BRANCH"


cd app/microservice-eventsourcing
git checkout $BRANCH
git pull

cat docker-compose-service-cicd.yml

docker compose -f docker-compose-service-cicd.yml down
docker compose -f docker-compose-service-cicd.yml pull
docker compose -f docker-compose-service-cicd.yml up -d
docker system prune -af