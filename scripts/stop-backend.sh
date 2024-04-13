#!/bin/bash

if [ -f /root/SE/auto-deployment/backend/app.pid ]; then
  xargs kill < /root/SE/auto-deployment/backend/app.pid
  sleep 10
  screen -S SE-backend -X stuff $'exit\n'
  sleep 5
fi