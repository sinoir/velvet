#!/bin/sh
#
# jenkins script for injecting S3 url for alpha build into assets/s3.properties right before build
#
set -e # stop if command returns non zero
echo injecting S3 url into assets/s3.properties

GIT_BRANCH_NO_PATH=$(echo ${GIT_BRANCH} | sed -e 's,.*/\(.*\),\1,')

#inject URL
S3_PROPS_PATH=${WORKSPACE}/Delectable/app/src/main/assets/s3.properties
LINK_VALUE="https://s3.amazonaws.com/fermentationtank/android/${GIT_BRANCH_NO_PATH}-alpha.html"
sed -i "" -E "s|(S3_LINK=)[^=]*$|\1${LINK_VALUE}|" "${S3_PROPS_PATH}"