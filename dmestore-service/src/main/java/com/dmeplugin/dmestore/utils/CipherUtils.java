package com.dmeplugin.dmestore.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Utils to encrypt/decrypt eSight password string
 */
public class CipherUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(CipherUtils.class);
  private static final int IV_SIZE = 16;

  public static final int KEY_SIZE = 8;

  public static final int BIT_SIZE = 32;

  /**
   * 迭代次数
   */
  public static final int PBKDF2_ITERATIONS = 10000;

  private static final String KEY = "668DAFB758034A97";


  public static String encryptString(String sSrc) {

    return aesEncode(sSrc, KEY);
  }

  //    * 加密用的Key 可以用26个字母和数字组成
//    * 此处使用AES-128-CBC加密模式，key需要为16位。
  public static String aesEncode(String sSrc, String key) {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      byte[] raw;
      raw = key.getBytes("utf-8");
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      byte[] ivBytes = getSafeRandom(IV_SIZE);
      IvParameterSpec iv = new IvParameterSpec(ivBytes);// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
      cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
      byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
      return new BASE64Encoder().encode(unitByteArray(ivBytes, encrypted));// 此处使用BASE64做转码。
    } catch (Exception e) {
      LOGGER.error("Failed to encode AES");
    }
    return null;
  }

  public static String decryptString(String sSrc) {

    return aesDncode(sSrc, KEY);
  }

  public static String aesDncode(String sSrc, String key) {
    try {
      byte[] raw = key.getBytes("utf-8");
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      byte[] sSrcByte = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密
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

  private static byte[] getSafeRandom(int num) throws NoSuchAlgorithmException {
    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
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
