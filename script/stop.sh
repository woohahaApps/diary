#!/bin/bash

# check if process exist
PID=$(ps -ef | grep -v grep | grep 'diary.jar')
echo current PID = ${PID}

get_pid=$(echo ${PID} | cut -d " " -f2)

# stop service if current process exist
if [ -n "${get_pid}" ]; then # -n : Checks if the length of a string is nonzero
  # stop service
  systemctl stop diary.service
  echo service stopped: ${get_pid}
fi

# check executed process
NEWPID=$(ps -ef | grep -v grep | grep 'diary.jar')
echo NEWPID = ${NEWPID}

# check service status
systemctl status diary.service

if [ -n "${NEWPID}" ]; then # -n : Checks if the length of a string is nonzero
  exit 1; # return error
fi

exit 0 # success