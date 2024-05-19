#!/bin/bash

screen -dmS SE-fileServer
screen -S SE-fileServer -X stuff $'cd /home/shadowfall/Documents/Software_Engineering/auto-deployment/fileServer\n'
screen -S SE-fileServer -X stuff $'java -jar fileServer-0.0.1-SNAPSHOT.jar\n'
