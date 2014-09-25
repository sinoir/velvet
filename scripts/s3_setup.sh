#!/bin/sh
#
# jenkins script to get files ready for s3 upload. Copies apk, html and version.properties file into local S3 folder.
#
set -e # stop if command returns non zero
echo packaging apk for S3 enterprise distribution

GIT_BRANCH_NO_PATH=$(echo ${GIT_BRANCH} | sed -e 's,.*/\(.*\),\1,')
ENTERPRISE_PRODUCT_DIRECTORY=${WORKSPACE}/BuildsForS3
mkdir -p "${ENTERPRISE_PRODUCT_DIRECTORY}"

#copy apk
cp "${WORKSPACE}/Delectable/app/build/outputs/apk/app-debug.apk" "${ENTERPRISE_PRODUCT_DIRECTORY}/${GIT_BRANCH_NO_PATH}-debug.apk"
cp "${WORKSPACE}/Delectable/app/build/outputs/apk/app-alpha.apk" "${ENTERPRISE_PRODUCT_DIRECTORY}/${GIT_BRANCH_NO_PATH}-alpha.apk"
cp "${WORKSPACE}/Delectable/app/build/outputs/apk/app-release.apk" "${ENTERPRISE_PRODUCT_DIRECTORY}/${GIT_BRANCH_NO_PATH}-release.apk"

#copy version.properties file
cp "${WORKSPACE}/Delectable/app/version.properties" "${ENTERPRISE_PRODUCT_DIRECTORY}/version.properties"

# copy html template
DOWNLOAD_LANDING_HTML=${ENTERPRISE_PRODUCT_DIRECTORY}/${GIT_BRANCH_NO_PATH}.html
DOWNLOAD_LANDING_HTML_ALPHA=${ENTERPRISE_PRODUCT_DIRECTORY}/${GIT_BRANCH_NO_PATH}-alpha.html
DOWNLOAD_LANDING_HTML_RELEASE=${ENTERPRISE_PRODUCT_DIRECTORY}/${GIT_BRANCH_NO_PATH}-release.html
cp "${WORKSPACE}/scripts/android_download.html" "${DOWNLOAD_LANDING_HTML}"
cp "${WORKSPACE}/scripts/android_download.html" "${DOWNLOAD_LANDING_HTML_ALPHA}"
cp "${WORKSPACE}/scripts/android_download.html" "${DOWNLOAD_LANDING_HTML_RELEASE}"

#inject values into html template
#debug
sed -i "" "s@RELEASE_NAME@${GIT_BRANCH_NO_PATH}-debug@g" "${DOWNLOAD_LANDING_HTML}"
sed -i "" "s@DOWNLOAD_URL@https://s3.amazonaws.com/fermentationtank/android/${GIT_BRANCH_NO_PATH}-debug.apk@g" "${DOWNLOAD_LANDING_HTML}"
sed -i "" "s@RELEASE_NAME@${GIT_BRANCH_NO_PATH}-debug@g" "${DOWNLOAD_LANDING_HTML}"
sed -i "" "s@LAST_UPDATED@last updated ${BUILD_ID}@g" "${DOWNLOAD_LANDING_HTML}"

#alpha
sed -i "" "s@RELEASE_NAME@${GIT_BRANCH_NO_PATH}-alpha@g" "${DOWNLOAD_LANDING_HTML_ALPHA}"
sed -i "" "s@DOWNLOAD_URL@https://s3.amazonaws.com/fermentationtank/android/${GIT_BRANCH_NO_PATH}-alpha.apk@g" "${DOWNLOAD_LANDING_HTML_ALPHA}"
sed -i "" "s@RELEASE_NAME@${GIT_BRANCH_NO_PATH}-alpha@g" "${DOWNLOAD_LANDING_HTML_ALPHA}"
sed -i "" "s@LAST_UPDATED@last updated ${BUILD_ID}@g" "${DOWNLOAD_LANDING_HTML_ALPHA}"

#release
sed -i "" "s@RELEASE_NAME@${GIT_BRANCH_NO_PATH}-release@g" "${DOWNLOAD_LANDING_HTML_RELEASE}"
sed -i "" "s@DOWNLOAD_URL@https://s3.amazonaws.com/fermentationtank/android/${GIT_BRANCH_NO_PATH}-release.apk@g" "${DOWNLOAD_LANDING_HTML_RELEASE}"
sed -i "" "s@RELEASE_NAME@${GIT_BRANCH_NO_PATH}-release@g" "${DOWNLOAD_LANDING_HTML_RELEASE}"
sed -i "" "s@LAST_UPDATED@last updated ${BUILD_ID}@g" "${DOWNLOAD_LANDING_HTML_RELEASE}"