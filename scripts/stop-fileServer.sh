#!/bin/bash

if [ -f /root/SE/auto-deployment/fileServer/app.pid ]; then
  xargs kill < /root/SE/auto-deployment/fileServer/app.pid
  sleep 10
  screen -S SE-fileServer -X stuff $'exit\n'
  sleep 5
fi