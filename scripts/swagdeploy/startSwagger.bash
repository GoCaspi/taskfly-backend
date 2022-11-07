#!/bin/bash
currentPath=`pwd`
npm i
rm output -r
mkdir output
node app.js
sudo docker rm swagger
sudo docker run --name swagger -p 8000:8080 -v $currentPath/output:/foo -e SWAGGER_JSON=/foo/collection.yml swaggerapi/swagger-ui



