package com.hm.viewdemo.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 签名工具类（Java 版本）
 * 功能：拼接 openid + nickname + avatar + 产品密钥，生成 32 位十六进制 MD5 签名
 */
public class MD5Utils2 {

    /**
     * 生成用户 MD5 签名（核心方法）
     * @param openid 用户唯一标识（可为 null 或空字符串，空值自动转为 ""）
     * @param nickname 用户昵称（可为 null 或空字符串，空值自动转为 ""）
     * @param avatar 用户头像地址（可为 null 或空字符串，空值自动转为 ""）
     * @param appSecret 产品密钥（必填，不可为 null 或空）
     * @return 32 位大写 MD5 签名；若参数非法（如 appSecret 为空）或加密失败，返回空字符串
     */
    public static String generateUserSign(String openid, String nickname, String avatar, String appSecret) {
        // 1. 空参数处理：null 转为空字符串，避免空指针
        String openidStr = openid == null ? "" : openid;
        String nicknameStr = nickname == null ? "" : nickname;
        String avatarStr = avatar == null ? "" : avatar;

        // 2. 校验产品密钥（必填）
        if (appSecret == null || appSecret.trim().isEmpty()) {
            throw new IllegalArgumentException("产品密钥 appSecret 不能为空！");
        }
        String appSecretStr = appSecret.trim();

        // 3. 按顺序拼接参数：openid + nickname + avatar + appSecret
        String sourceStr = openidStr + nicknameStr + avatarStr + appSecretStr;

        // 4. 执行 MD5 加密并返回 32 位十六进制结果
        return md5Encode(sourceStr);
    }

    /**
     * 通用 MD5 加密方法
     * @param input 待加密的字符串（UTF-8 编码）
     * @return 32 位大写十六进制 MD5 字符串；加密失败返回空字符串
     */
    public static String md5Encode(String input) {
        try {
            // 1. 获取 MD5 消息摘要实例（Java 标准算法，无需额外依赖）
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            // 2. 将字符串转为 UTF-8 字节数组（解决中文乱码问题）
            byte[] inputBytes = input.getBytes("UTF-8");

            // 3. 执行加密运算：生成 16 位字节数组的 MD5 摘要
            byte[] digestBytes = md5.digest(inputBytes);

            // 4. 将 16 位字节数组转为 32 位十六进制字符串
            return bytesToHex(digestBytes);

        } catch (NoSuchAlgorithmException e) {
            // 理论上不会触发（MD5 是 Java 内置算法）
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            // 捕获编码异常等其他异常
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 将字节数组转为 32 位十六进制字符串（补零保证长度）
     * @param bytes 加密后的字节数组（MD5 加密后为 16 位）
     * @return 32 位大写十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexBuilder = new StringBuilder();
        for (byte b : bytes) {
            // 字节转十六进制：& 0xFF 确保为正数（避免负数转十六进制出现乱码）
            String hex = Integer.toHexString(b & 0xFF);
            // 不足两位补 0（如 0x1 → "01"，保证最终结果为 32 位）
            if (hex.length() == 1) {
                hexBuilder.append("0");
            }
            hexBuilder.append(hex);
        }
        // 返回大写结果（如需小写，改为 hexBuilder.toString().toLowerCase()）
        return hexBuilder.toString().toUpperCase();
    }
}