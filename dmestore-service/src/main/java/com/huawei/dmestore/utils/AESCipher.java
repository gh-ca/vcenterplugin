package com.huawei.dmestore.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


public class AESCipher {
    private static final Logger LOG = LoggerFactory.getLogger(AESCipher.class);

    private CipherConfig cipherConfig;

    private RootKeyGenerator rootKeyGenerator;

    public CipherConfig getCipherConfig() {
        return cipherConfig;
    }

    public void setCipherConfig(CipherConfig cipherConfig) {
        this.cipherConfig = cipherConfig;
    }

    public RootKeyGenerator getRootKeyGenerator() {
        return rootKeyGenerator;
    }

    public void setRootKeyGenerator(RootKeyGenerator rootKeyGenerator) {
        this.rootKeyGenerator = rootKeyGenerator;
    }

    private  HashMap<Integer, String> init() {
        HashMap<Integer, String> workkeys = new HashMap<>();
        //String workkey = System.getProperty("cipher.aes.workKey");
        String workkey=cipherConfig.getAes().getWorkKey();
        String[] array = StringUtils.delimitedListToStringArray(workkey, ",");
        for (String key : array) {
            if (!StringUtils.isEmpty(key)) {
                int index = key.indexOf("&");
                workkeys.put(Integer.valueOf(key.substring(0, index)), key.substring(index + 1));
            }
        }
        return workkeys;
    }


    private  byte[] decryptWorkKey(String workKey) {
        try {
            byte[] rootkey = rootKeyGenerator.generateRootKey();
            //String keyAlgorithm = System.getProperty("cipher.aes.key.algorithm", "AES");
            String keyAlgorithm=cipherConfig.getAes().getKey().getAlgorithm();
            SecretKeySpec secretKeySpec = new SecretKeySpec(rootkey, keyAlgorithm);
            //String delimiter = System.getProperty("cipher.delimiter", "&");
            String delimiter=cipherConfig.getDelimiter();
            String[] array = StringUtils.delimitedListToStringArray(workKey, delimiter);
            if (2 != array.length) {
                LOG.error("Delimiter workkey to string array,array length is not two");
                throw new IllegalArgumentException("workkey illegal");
            }
            byte[] ivBytes = CipherUtil.parseHexstr2Byte(array[0]);
            //System.out.println("ivbeytelenth="+ivBytes.length);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
            byte[] encryptWorkKey = CipherUtil.parseHexstr2Byte(array[1]);
            //System.out.println("encryptWorkKey="+encryptWorkKey.length);
            //String algorthm = System.getProperty("cipher.aes.algorithm", "AES/CBC/PKCS5Padding");
            String algorthm=cipherConfig.getAes().getAlgorithm();
            Cipher cipher = Cipher.getInstance(algorthm);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(encryptWorkKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            LOG.error("decrypt workkey failed");
            //e.printStackTrace();
            return null;
        }
    }
    public  String encryptByAES(String rawData) {
        return encryptByAES(rawData,1);
    }

    public  String encryptByAES(String rawData, Integer keyID) {
        if (StringUtils.isEmpty(rawData)) {
            LOG.warn("the raw data is empty");
            return null;
        }
        try {
            HashMap<Integer, String> workKeys = init();
            if (workKeys.isEmpty() || StringUtils.isEmpty(workKeys.get(keyID))) {
                LOG.error("work key used to encrypt is empty");
                return null;
            }
            String workKey = workKeys.get(keyID);
            byte[] decryptWorkKey = decryptWorkKey(workKey);
            if (null==decryptWorkKey)
            {
                throw new IllegalArgumentException("work key decrypt error");
            }
           // String keyAlgorithm = System.getProperty("cipher.aes.key.algorithm", "AES");
            String keyAlgorithm=cipherConfig.getAes().getKey().getAlgorithm();
            SecretKeySpec secretKeySpec = new SecretKeySpec(decryptWorkKey, keyAlgorithm);
            byte[] iv = CipherUtil.initIV();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            //String algorithm = System.getProperty("cipher.aes.algorithm", "AES/CBC/PKCS5Padding");
            String algorithm=cipherConfig.getAes().getAlgorithm();
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encryptedData = cipher.doFinal(rawData.getBytes());
            String striv = CipherUtil.parseByte2Hexstr(iv);
            String strData = CipherUtil.parseByte2Hexstr(encryptedData);
            //String delimiter = System.getProperty("cipher.delimiter", "&");
            String delimiter=cipherConfig.getDelimiter();
            return String.valueOf(keyID) + delimiter + striv + delimiter + strData;
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException e) {
            LOG.error("encrypt rawdata error");
            return null;
        }
    }

    public  String decryptByAES(String rawData) {
        if (StringUtils.isEmpty(rawData)) {
            LOG.warn("the raw data is empty");
            return null;
        }
        try {
            //String delimiter = System.getProperty("cipher.delimiter", "&");
            String delimiter=cipherConfig.getDelimiter();
            String[] array = StringUtils.delimitedListToStringArray(rawData, delimiter);
            if (3 != array.length) {
                LOG.error("delimite rawdata length is not three");
                throw new IllegalArgumentException("rawData illegal");
            }
            HashMap<Integer, String> workKeys = init();
            String workkey = workKeys.get(Integer.valueOf(array[0]));
            if (StringUtils.isEmpty(workkey)) {
                LOG.error("work key used to decrypt is empty");
                return null;
            }
            byte[] decryptworkkey = decryptWorkKey(workkey);
            if (null==decryptworkkey)
            {
                throw new IllegalArgumentException("work key decrypt error");
            }
            //String keyAlgorithm = System.getProperty("cipher.aes.key.algorithm", "AES");
            String keyAlgorithm=cipherConfig.getAes().getKey().getAlgorithm();
            SecretKeySpec secretKeySpec = new SecretKeySpec(decryptworkkey, keyAlgorithm);
            byte[] iv = CipherUtil.parseHexstr2Byte(array[1]);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            //String algorithm = System.getProperty("cipher.aes.algorithm", "AES/CBC/PKCS5Padding");
            String algorithm=cipherConfig.getAes().getAlgorithm();
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] decryptData = cipher.doFinal(CipherUtil.parseHexstr2Byte(array[2]));
            return new String(decryptData);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException e) {
            LOG.error("decrypt error");
            return null;
        }
    }


}
