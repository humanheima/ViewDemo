# 获取 APK 文件路径
#APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
#
## 确保 APK 文件存在
#if [ -f "$APK_PATH" ]; then
#    # 使用 Python 生成二维码
#    python3 -c "import qrcode; qr = qrcode.QRCode(version=1, box_size=10, border=4); qr.add_data('file://$APK_PATH'); qr.make(fit=True); img = qr.make_image(fill='black', back_color='white'); img.save('qrcode.png')"
#else
#    echo "Error: APK file not found at $APK_PATH"
#    exit 1
#fi



javac -cp ci_tools/zxing-core-3.5.3.jar:ci_tools/zxing-javase-3.5.3.jar ci_tools/GenerateQRCode.java
APK_URL="app/build/outputs/apk/debug/app-debug.apk"
QR_IMAGE_PATH="ci_tools/qrcode.png"
java -cp ci_tools:ci_tools/zxing-core-3.5.3.jar:ci_tools/zxing-javase-3.5.3.jar GenerateQRCode "$APK_URL" "$QR_IMAGE_PATH"