#!/bin/bash

if [ -f /root/SE/auto-deployment/fileServer/app.pid ]; then
  xargs kill < /root/SE/auto-deployment/fileServer/app.pid
  sleep 10
fi

sessions=$(screen -ls | grep "\.SE-fileServer\s" | awk -F '.' '{print $1}')

for session in $sessions; do
  screen -X -S "$session" quit
done
