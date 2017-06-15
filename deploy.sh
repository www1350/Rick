#!/bin/sh
docker build -t absurd/rick-app .
docker run -ti \
-p 8080:8080 \
--name rick_app \
absurd/rick-app:latest \
