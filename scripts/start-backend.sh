#!/bin/bash

screen -dmS SE-backend
screen -S SE-backend -X stuff $'cd /root/SE/auto-deployment/backend\n'
screen -S SE-backend -X stuff $'java -jar backend-0.0.1-SNAPSHOT.jar\n'
