package com.dmeplugin.dmestore.utils;

import com.dmeplugin.dmestore.exception.VcenterException;
import com.sun.jna.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Utils to encrypt/decrypt eSight password string
 */
public class CipherUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(CipherUtils.class);

  private static final String KEY = "668DAFB758034A97";

  // public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";

  private static final int IV_SIZE = 16;

  public static final int KEY_SIZE = 8;

  public static final int BIT_SIZE = 32;

  /**
   * 生成密文的长度
   */
  public static final int HASH_BIT_SIZE = 128 * 2;

  /**
   * 迭代次数
   */
  public static final int PBKDF2_ITERATIONS = 10000;

  public static final String DEFAULT_CHARSET = "UTF-8";

  public static String aesEncode(String sSrc) {
    String key = null;
    try {
      key = getWorkKey();
    } catch (Exception e) {
      LOGGER.error("Failed to encode AES");
    }
    if (key == null) {
      return null;
    }
    return aesEncode(sSrc, key);
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

  public static String aesDncode(String sSrc) {
    String key = null;
    try {
      key = getWorkKey();
    } catch (Exception e) {
      LOGGER.error("Failed to decode AES");
    }
    if (key == null) {
      return null;
    }
    return aesDncode(sSrc, key);
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

  public static String getSafeRandomToString(int num) throws NoSuchAlgorithmException {
    return toHex(getSafeRandom(num));
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


  /**
   * 生成工作密钥密文
   *
   * @param key 明文
   * @param salt 盐值
   */
  public static String getEncryptedKey(String key, String salt)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    Pointer out = new Memory(256);
    Function function = Function.getFunction(OpenSSLLib.INSTANCE.EVP_sha256());

    int exit = OpenSSLLib.INSTANCE
        .PKCS5_PBKDF2_HMAC(key, key.length(), salt, salt.length(), PBKDF2_ITERATIONS,
            function, BIT_SIZE, out);
    if (exit == 0) {
      throw new VcenterException("PKCS5_PBKDF2_HMAC error!");
    }
    byte[] c = out.getByteArray(0, BIT_SIZE);
    return toHex(c);
  }

  /**
   *
   *
   * @param array the byte array to convert
   * @return a length*2 character string encoding the byte array
   */
  private static String toHex(byte[] array){
    String TRANSFORMSTR = "0123456789abcdef";
    StringBuilder stringBuilder = new StringBuilder();
    for(byte bufferByte: array){
        stringBuilder.append(TRANSFORMSTR.charAt((bufferByte&(0xf0))>>4));
        stringBuilder.append(TRANSFORMSTR.charAt(bufferByte&(0x0f)));
    }
    return stringBuilder.toString();
  } 


  public static String getBaseKey()
      throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
    String XOrKey = getXOrString();
    return getEncryptedKey(XOrKey, KEY).substring(0, 16);
  }

  private static String getWorkKey()
      throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
    String baseKey = getBaseKey();
    String workKey = FileUtils.getKey(FileUtils.WORK_FILE_NAME);
    if (workKey == null) {
      String key = getSafeRandomToString(KEY_SIZE);
      workKey = CipherUtils.aesEncode(key, baseKey);
      FileUtils.saveKey(workKey, FileUtils.WORK_FILE_NAME);
    }
    return aesDncode(workKey, baseKey);
  }

  private static String getXOrString()
      throws UnsupportedEncodingException, NoSuchAlgorithmException {
    byte[] hardKey = KEY.getBytes("utf-8");
    String fileStringKey = FileUtils.getKey(FileUtils.BASE_FILE_NAME);
    if (fileStringKey == null) {
      fileStringKey = getSafeRandomToString(KEY_SIZE);
      FileUtils.saveKey(fileStringKey, FileUtils.BASE_FILE_NAME);
    }
    byte[] fileKey = fileStringKey.getBytes("utf-8");
    byte[] XOrKey = new byte[hardKey.length];
    for (int i = 0; i < XOrKey.length; i++) {
      XOrKey[i] = (byte) (hardKey[i] ^ fileKey[i]);
    }
    return new String(XOrKey, "utf-8");
  }

  public interface OpenSSLLib extends Library {

    OpenSSLLib INSTANCE = (OpenSSLLib) Native
        .loadLibrary(Platform.isWindows() ? "libeay32" : "crypto", OpenSSLLib.class);//加载动态库文件


    int PKCS5_PBKDF2_HMAC_SHA1(String password, int len, String salt, int slatLen, int iter,
        int keyLen, Pointer out);

    /**
     * int PKCS5_PBKDF2_HMAC(const char *pass, int passlen, const unsigned char *salt, int saltlen,
     * int iter, const EVP_MD *digest, int keylen, unsigned char *out);
     */
    int PKCS5_PBKDF2_HMAC(String password, int len, String salt, int slatLen, int iter,
        Function evp, int keyLen,
        Pointer out);

    Pointer EVP_sha256();

  }
}
