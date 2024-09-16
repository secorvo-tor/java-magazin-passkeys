# Passkey Demo

## Build local

The build process has to follow the order backend - frontend - app

    ./mvnw clean package

Build frontend

    ./mvnw -P frontend clean package

Build app

    ./mvnw -P build-app package

Build and run docker image

    docker compose -f etc/docker/docker-compose.local.yml up -d

After following these steps, the app should be available at 
[http://localhost:8080](http://localhost:8080)

## Build prod - all in one

Dockerfile to build the application from scratch.
Very wasteful (takes a lot of disk space) and slow.

    docker compose -f etc/docker/docker-compose.prod.yml up -d
