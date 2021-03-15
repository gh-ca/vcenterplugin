package com.huawei.dmestore.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utils to encrypt/decrypt eSight password string
 */

public class CipherUtils {

    private AESCipher aesCipher;

    private static final Logger LOGGER = LoggerFactory.getLogger(CipherUtils.class);

    //private static final int IV_SIZE = 16;

    private  String multitypeutil;

    private  String cipheralgorithm;

    private  String securerandomalgorithm;

    private  String keyalgorithm;

    private  String ivsize;

    public String getMultitypeutil() {
        return multitypeutil;
    }

    public void setMultitypeutil(String multitypeutil) {
        this.multitypeutil = multitypeutil;
    }

    public String getCipheralgorithm() {
        return cipheralgorithm;
    }

    public void setCipheralgorithm(String cipheralgorithm) {
        this.cipheralgorithm = cipheralgorithm;
    }

    public String getSecurerandomalgorithm() {
        return securerandomalgorithm;
    }

    public void setSecurerandomalgorithm(String securerandomalgorithm) {
        this.securerandomalgorithm = securerandomalgorithm;
    }

    public String getKeyalgorithm() {
        return keyalgorithm;
    }

    public void setKeyalgorithm(String keyalgorithm) {
        this.keyalgorithm = keyalgorithm;
    }

    public String getIvsize() {
        return ivsize;
    }

    public void setIvsize(String ivsize) {
        this.ivsize = ivsize;
    }

    public AESCipher getAesCipher() {
        return aesCipher;
    }

    public void setAesCipher(AESCipher aesCipher) {
        this.aesCipher = aesCipher;
    }

    public  String encryptString(String sSrc) {

        //return aesEncode(sSrc, multitypeutil);
        return aesCipher.encryptByAES(sSrc);
    }

    public  String aesEncode(String sSrc, String key) {
        try {
            Cipher cipher = Cipher.getInstance(cipheralgorithm);
            byte[] raw;
            raw = key.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, keyalgorithm);
            byte[] ivBytes = getSafeRandom(Integer.parseInt(ivsize));
            // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
            // 此处使用BASE64做转码。
            return new BASE64Encoder().encode(unitByteArray(ivBytes, encrypted));
        } catch (Exception e) {
            LOGGER.error("Failed to encode AES");
        }
        return null;
    }

    public  String decryptString(String sSrc) {
        //return aesDncode(sSrc, multitypeutil);
        return aesCipher.decryptByAES(sSrc);
    }

    public  String aesDncode(String sSrc, String key) {
        try {
            byte[] raw = key.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, keyalgorithm);
            Cipher cipher = Cipher.getInstance(cipheralgorithm);
            // 先用base64解密
            byte[] sSrcByte = new BASE64Decoder().decodeBuffer(sSrc);
            byte[] ivBytes = splitByteArray(sSrcByte, 0, 16);
            byte[] encrypted = splitByteArray(sSrcByte, 16, sSrcByte.length - 16);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(encrypted);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception e) {
            LOGGER.error("Failed to decode aes");
        }

        // 如果有错就返加null
        return null;
    }

    private  byte[] getSafeRandom(int num) throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance(securerandomalgorithm);
        byte[] b = new byte[num];
        random.nextBytes(b);
        return b;
    }

    /**
     * 合并byte数组
     */
    public static byte[] unitByteArray(byte[] byte1, byte[] byte2) {
        byte[] unitByte = new byte[byte1.length + byte2.length];
        System.arraycopy(byte1, 0, unitByte, 0, byte1.length);
        System.arraycopy(byte2, 0, unitByte, byte1.length, byte2.length);
        return unitByte;
    }

    /**
     * 拆分byte数组
     */
    public static byte[] splitByteArray(byte[] byte1, int start, int end) {
        byte[] splitByte = new byte[end];
        System.arraycopy(byte1, start, splitByte, 0, end);
        return splitByte;
    }

}
