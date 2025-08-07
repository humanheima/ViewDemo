#!/bin/bash

# 设置默认值
VERSION_NAME=${1:-1.0.0}
VERSION_CODE=${2:-1}
BUILD_TYPE=${3:-debug}

echo "Starting build with VERSION_NAME=$VERSION_NAME, VERSION_CODE=$VERSION_CODE, BUILD_TYPE=$BUILD_TYPE"

if [ "$BUILD_TYPE" = "debug" ]; then
    echo "Building debug package..."
    ./gradlew assembleDebug
elif [ "$BUILD_TYPE" = "release" ]; then
    echo "Building release package..."
    ./gradlew assembleRelease
else
    echo "Error: Invalid BUILD_TYPE. Use 'debug' or 'release'."
    exit 1
fi


APK_PATH="${WORKSPACE}/app/build/outputs/apk/debug/app-debug.apk"
python3 -m pip install qrcode[pil] --user
python3 -c "import qrcode; qrcode.make('${APK_PATH}').save('${WORKSPACE}/ci_tools/another_qrcode.png')"
