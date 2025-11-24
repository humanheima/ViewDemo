package com.hm.viewdemo.activity;

import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 严格遵循「2.3 密文传递」规则的 AES 加密工具类
 * 原生Java实现，无第三方依赖，适配AES-128-CBC/PKCS#7/Base64替换规则
 */
public class LoginStateEncryptUtils {

    private static final String TAG = "LoginStateEncryptUtils";

    /**
     * 生成登录态密文字符串（核心方法）
     * @param productKey 产品密钥（从产品设置获取）
     * @param productId 产品ID（从产品设置获取）
     * @param openid 用户唯一标识（接入方生成）
     * @param nickname 用户昵称
     * @param avatar 用户头像（HTTPS链接）
     * @param expiredAt 过期时间（10位unix时间戳字符串，"0"表示不过期）
     * @return 符合规则的密文字符串（user_data参数值）
     * @throws Exception 加密异常
     */
    public static String generateEncryptedUserData(
            String productKey,
            String productId,
            String openid,
            String nickname,
            String avatar,
            String expiredAt
    ) throws Exception {
        // 1. 校验必填字段
        validateRequiredFields(openid, nickname, avatar, expiredAt);
        // 2. 校验头像链接（必须HTTPS）
        validateAvatarHttps(avatar);

        // 3. 组装登录态信息（含nonce随机值）
        Map<String, String> loginState = new HashMap<>();
        loginState.put("openid", openid);
        loginState.put("nickname", nickname);
        loginState.put("avatar", avatar);
        loginState.put("nonce", generateNonce()); // 生成随机nonce
        loginState.put("expired_at", expiredAt);

        // 4. 转JSON字符串（原生拼接，无第三方依赖）
        String jsonStr = buildJsonString(loginState);

        // 5. 处理密钥和IV（按规则补足16位）
        String key = padTo16Chars(productKey); // 产品密钥补足16位
        String iv = padTo16Chars(productId + productKey); // 产品ID+密钥补足16位

        // 6. AES-128-CBC加密（PKCS#7填充，原生PKCS5兼容）
        byte[] encryptBytes = aesCbcEncrypt(jsonStr.getBytes(StandardCharsets.UTF_8), key, iv);

        // 7. Base64编码 + 字符替换 + 删右侧=
        String base64Str = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            base64Str = Base64.getEncoder().encodeToString(encryptBytes);
        }
        String encryptedData = base64Str.replace("+", "-")
                .replace("/", "_")
                .replaceAll("=+$", ""); // 删除右侧所有=

        return encryptedData;
    }

    /**
     * AES-128-CBC 加密（原生实现，PKCS#7填充兼容）
     */
    private static byte[] aesCbcEncrypt(byte[] data, String keyStr, String ivStr) throws Exception {
        // 密钥和IV转字节数组（UTF-8）
        byte[] keyBytes = keyStr.getBytes(StandardCharsets.UTF_8);
        byte[] ivBytes = ivStr.getBytes(StandardCharsets.UTF_8);

        // 初始化密钥和IV
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        // AES-128-CBC + PKCS5Padding（兼容PKCS#7）
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        return cipher.doFinal(data);
    }

    /**
     * 字符串补足至16位（右侧用=填充）
     */
    private static String padTo16Chars(String str) {
        if (str == null) str = "";
        int length = str.length();
        if (length >= 16) {
            return str.substring(0, 16); // 超过16位取前16位
        }
        // 不足16位，右侧补=
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < 16) {
            sb.append("=");
        }
        return sb.toString();
    }

    /**
     * 生成随机nonce（UUID简化，保证唯一性）
     */
    private static String generateNonce() {
        //return UUID.randomUUID().toString().replace("-", "");
        return "0";
    }

    /**
     * 校验必填字段（非空）
     */
    private static void validateRequiredFields(String openid, String nickname, String avatar, String expiredAt) {
        if (openid == null || openid.trim().isEmpty()) {
            throw new IllegalArgumentException("openid为必填字段，不能为空");
        }
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("nickname为必填字段，不能为空");
        }
        if (avatar == null || avatar.trim().isEmpty()) {
            throw new IllegalArgumentException("avatar为必填字段，不能为空");
        }
        if (expiredAt == null || expiredAt.trim().isEmpty()) {
            throw new IllegalArgumentException("expired_at为必填字段，不能为空");
        }
        // 校验expired_at格式（10位数字或0）
        if (!expiredAt.equals("0") && !expiredAt.matches("\\d{10}")) {
            throw new IllegalArgumentException("expired_at必须是10位数字字符串或\"0\"");
        }
    }

    /**
     * 校验头像链接是否为HTTPS
     */
    private static void validateAvatarHttps(String avatar) {
        if (avatar != null && !avatar.trim().startsWith("https://")) {
            throw new IllegalArgumentException("avatar必须是HTTPS协议的图片链接");
        }
    }

    /**
     * 原生拼接JSON字符串（替代第三方JSON库）
     */
    private static String buildJsonString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder("{");
        int count = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (count > 0) {
                sb.append(",");
            }
            // 转义双引号（避免JSON格式错误）
            String value = entry.getValue().replace("\"", "\\\"");
            sb.append("\"").append(entry.getKey()).append("\":\"").append(value).append("\"");
            count++;
        }
        sb.append("}");
        return sb.toString();
    }

    // 测试示例
    public static String main() {
        String queryString = null;
        try {
            // 模拟参数（替换为实际产品密钥/ID）
            String productKey = "12345555xxx"; // 不足16位，会自动补=
            String productId = "790831xx";
            String openid = "246245927461851136";
            String nickname = "戏友5790";
            String avatar = "https://staticcdn-test.rolepub.com/13af773c478235e6228a6907a3b9da3c.jpg"; // 必须HTTPS
            String expiredAt = "0"; // 10位时间戳（2025-01-01）

            // 生成密文（user_data参数值）
            String userData = generateEncryptedUserData(
                    productKey, productId, openid, nickname, avatar, expiredAt
            );
            Log.d(TAG, "生成的user_data密文：" + userData);

            // 模拟构造HTTP请求（POST，queryString或请求体传递user_data）

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                queryString = "user_data=" + java.net.URLEncoder.encode(userData, StandardCharsets.UTF_8);
            }else {
                queryString = "user_data=" + java.net.URLEncoder.encode(userData, "UTF-8");
            }
            Log.d(TAG, "HTTP请求queryString：" + queryString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryString;
    }
}