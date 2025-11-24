package com.hm.viewdemo.util

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by p_dmweidu on 2025/11/24
 * Desc: 测试加密兔小巢
 */
object MD5Utils {
    /**
     * 生成 MD5 签名（32 位十六进制）
     * @param openid 用户唯一标识（可为空，空则传空字符串）
     * @param nickname 用户昵称（可为空，空则传空字符串）
     * @param avatar 用户头像地址（可为空，空则传空字符串）
     * @param appSecret 产品密钥（必填，从后台获取）
     * @return 32 位大写 MD5 签名（如需小写，将 toUpperCase() 改为 toLowerCase()）
     */
    fun generateUserSign(
        openid: String,
        nickname: String,
        avatar: String,
        appSecret: String
    ): String {
        // 1. 按顺序拼接参数（空参数保留空字符串，不跳过）
        val sourceStr = openid + nickname + avatar + appSecret
        return md5(sourceStr)
    }

    /**
     * 通用 MD5 加密方法
     * @param input 待加密字符串
     * @return 32 位大写十六进制结果
     */
    fun md5(input: String): String {
        try {
            // 2. 获取 MD5 消息摘要实例
            val messageDigest = MessageDigest.getInstance("MD5")
            // 3. 将字符串转为字节数组（UTF-8 编码，避免中文乱码）
            val inputBytes = input.toByteArray(charset("UTF-8"))
            // 4. 执行加密运算，得到加密后的字节数组（16 位）
            val digestBytes = messageDigest.digest(inputBytes)
            // 5. 将 16 位字节数组转为 32 位十六进制字符串
            return bytesToHex(digestBytes)
        } catch (e: NoSuchAlgorithmException) {
            // 理论上不会走到这里（MD5 是 Java 标准算法）
            e.printStackTrace()
            return ""
        }
    }

    /**
     * 将字节数组转为十六进制字符串
     */
    private fun bytesToHex(bytes: ByteArray): String {
        val stringBuilder = StringBuilder()
        for (byte in bytes) {
            // 字节转十六进制（& 0xFF 确保为正数）
            val hex = Integer.toHexString(byte.toInt() and 0xFF)
            // 不足两位补 0（如 0x1 转为 "01"，保证 32 位长度）
            if (hex.length == 1) {
                stringBuilder.append("0")
            }
            stringBuilder.append(hex)
        }
        // 转为大写（如需小写，改为 toString().toLowerCase()）
        return stringBuilder.toString().toUpperCase()
    }
}