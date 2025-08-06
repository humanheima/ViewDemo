# 获取 APK 文件路径
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"

# 确保 APK 文件存在
if [ -f "$APK_PATH" ]; then
    # 使用 Python 生成二维码
    python3 -c "import qrcode; qr = qrcode.QRCode(version=1, box_size=10, border=4); qr.add_data('file://$APK_PATH'); qr.make(fit=True); img = qr.make_image(fill='black', back_color='white'); img.save('qrcode.png')"
else
    echo "Error: APK file not found at $APK_PATH"
    exit 1
fi