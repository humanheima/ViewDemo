import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.File;
import java.nio.file.Paths;

public class GenerateQRCode {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java GenerateQRCode <url> <outputPath>");
            System.exit(1);
        }
        String url = args[0]; // APK 下载 URL
        String outputPath = args[1]; // 二维码图片输出路径
        int width = 200;
        int height = 200;

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", Paths.get(outputPath));
            System.out.println("QR code generated at: " + outputPath);
        } catch (WriterException e) {
            System.err.println("Error generating QR code: " + e.getMessage());
            System.exit(1);
        }
    }
}