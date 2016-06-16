#!/usr/bin/sh

cp -ai misc/server/etc/sysconfig/lympt /etc/sysconfig/
cp -ai misc/server/etc/systemd/system/lympt.service /etc/systemd/system/

cp -ai build/libs/LymptServer.jar /usr/bin
