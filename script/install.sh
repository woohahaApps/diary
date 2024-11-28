#!/bin/bash

SERVICE_NAME="diary.service"

# 현재 유닛의 활성화 상태 확인
is_enabled=$(systemctl is-enabled "$SERVICE_NAME" 2>/dev/null)

cp /var/webapp/diary/diary.service /etc/systemd/system/diary.service

systemctl daemon-reload

if [[ $is_enabled == "enabled" ]]; then
    echo "Service is already enabled."
else
    # 유닛을 활성화
    systemctl enable "$SERVICE_NAME"
    echo "Service has been enabled"
fi