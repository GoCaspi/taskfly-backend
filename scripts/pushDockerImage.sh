#!/bin/bash

sudo docker build --tag "$1" --build-arg VUE_APP_BASE_URL="$4"  .
sudo cat ./scripts/registrypw.txt | sudo docker login "$2" --username "$3" --password-stdin
sudo docker push "$1"