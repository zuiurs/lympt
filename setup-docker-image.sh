#!/usr/bin/sh

export REPO_ROOT="docker_files"
PREFIX="lympt/"

for path in `find -name Dockerfile | perl -pe 's|\n| |g'`
do
	REPO=${PREFIX}`echo ${path} | perl -pe 's|.*?$ENV{REPO_ROOT}/(.*)/(.*)/Dockerfile|$1|g'`
	TAG=`echo ${path} | perl -pe 's|.*?$ENV{REPO_ROOT}/(.*)/(.*)/Dockerfile|$2|g'`
	DCDIR=`echo ${path} | perl -pe 's|(.*)/Dockerfile|$1|g'`

	docker build -t ${REPO}:${TAG} ${DCDIR}
done
unset REPO_ROOT
