package com.dmeplugin.dmestore.utils;

import com.dmeplugin.dmestore.exception.VcenterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Utils to encrypt/decrypt eSight password string
 */
public class CipherUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(CipherUtils.class);


  private static int iterations = 65536  ;
  private static int keySize = 256;
  private static byte[] ivBytes;

  private static String salt;


  public static String encryptString(String text) throws Exception {
    return encrypt(text.toCharArray());
  }



  public static String encrypt(char[] plaintext) throws Exception {

    salt=getSalt();
    byte[] saltBytes = salt.getBytes();

    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec spec = new PBEKeySpec(plaintext, saltBytes, iterations, keySize);
    SecretKey secretKey = skf.generateSecret(spec);
    SecretKeySpec secretSpec = new SecretKeySpec(secretKey.getEncoded(),"AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secretSpec);
    AlgorithmParameters params = cipher.getParameters();
    ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
    byte[] encryptedTextBytes = cipher.doFinal(plaintext.toString().getBytes("UTF-8"));

    return DatatypeConverter.printBase64Binary(encryptedTextBytes);
  }

  public static String decryptString(String text) throws Exception {
    return decrypt(text.toCharArray());
  }

  public static String decrypt(char[] encryptedText) throws Exception {

    byte[] saltBytes = salt.getBytes("UTF-8");
    byte[] encryptedTextBytes = DatatypeConverter.parseBase64Binary(encryptedText.toString());

    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec spec = new PBEKeySpec(encryptedText, saltBytes, iterations, keySize);
    SecretKey secretkey = skf.generateSecret(spec);
    SecretKeySpec secretSpec = new SecretKeySpec(secretkey.getEncoded(),"AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secretSpec, new IvParameterSpec(ivBytes));

    byte[] decryptedTextBytes = null;

    try {
      decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
    }   catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    }   catch (BadPaddingException e) {
      e.printStackTrace();
    }

    return decryptedTextBytes.toString();

  }

  public static String getSalt() throws Exception {

    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt=new byte[20];
    sr.nextBytes(salt);
    return salt.toString();
  }










}
