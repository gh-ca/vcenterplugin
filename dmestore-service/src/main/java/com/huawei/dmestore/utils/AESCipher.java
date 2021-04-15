package com.huawei.dmestore.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AESCipher
 *
 * @since 2021-03-30
 */
public class AESCipher {
    private static final Logger LOG = LoggerFactory.getLogger(AESCipher.class);
    private static final int ARRAT_LENGTH_2 = 2;
    private static final int ARRAT_LENGTH_3 = 3;
    public static final int KEY_SIZE = 32;
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

  /*  private HashMap<Integer, String> init() {
        HashMap<Integer, String> workkeys = new HashMap<>();
        String workkey = cipherConfig.getAes().getWorkKey();
        String[] array = StringUtils.delimitedListToStringArray(workkey, ",");
        for (String key : array) {
            if (!StringUtils.isEmpty(key)) {
                int index = key.indexOf("&");
                workkeys.put(Integer.valueOf(key.substring(0, index)), key.substring(index + 1));
            }
        }
        return workkeys;
    }*/

    public  String genencryptWorkKey() {
        try {
            byte[] rootkey = rootKeyGenerator.generateRootKey();
            String keyAlgorithm = cipherConfig.getAes().getKey().getAlgorithm();
            SecretKeySpec secretKeySpec = new SecretKeySpec(rootkey, keyAlgorithm);

            byte[] iv = CipherUtil.initIV();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            String originworkkey=getSafeRandomToString(KEY_SIZE);
            String algorthm = cipherConfig.getAes().getAlgorithm();
            Cipher cipher = Cipher.getInstance(algorthm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            return new BASE64Encoder().encode(unitByteArray(iv,cipher.doFinal(CipherUtil.parseHexstr2Byte(originworkkey))));
        } catch (NoSuchAlgorithmException | InvalidKeyException
                | InvalidAlgorithmParameterException | NoSuchPaddingException
                | BadPaddingException | IllegalBlockSizeException e) {
            LOG.error("decrypt workkey failed");
            return null;
        }
    }

    private byte[] decryptWorkKey(String workKey) {
        try {
            byte[] rootkey = rootKeyGenerator.generateRootKey();
            String keyAlgorithm = cipherConfig.getAes().getKey().getAlgorithm();
            SecretKeySpec secretKeySpec = new SecretKeySpec(rootkey, keyAlgorithm);
            // 修改加密非强制性配置
            /*try {
                Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
                field.setAccessible(true);
                field.set(null, java.lang.Boolean.FALSE);
            } catch (Exception ex) {
                throw new IllegalArgumentException("workkey illegal");
            }*/
            /*String delimiter = cipherConfig.getDelimiter();
            String[] array = StringUtils.delimitedListToStringArray(workKey, delimiter);
            if (ARRAT_LENGTH_2 != array.length) {
                LOG.error("Delimiter workkey to string array,array length is not two");
                throw new IllegalArgumentException("workkey illegal");
            }*/
            byte[] sSrcByte = new BASE64Decoder().decodeBuffer(workKey);// 先用base64解密
            byte[] ivBytes = splitByteArray(sSrcByte, 0, 16);
            byte[] encrypted = splitByteArray(sSrcByte, 16, sSrcByte.length - 16);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

            String algorthm = cipherConfig.getAes().getAlgorithm();
            Cipher cipher = Cipher.getInstance(algorthm);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(encrypted);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | IOException e) {
            LOG.error("decrypt workkey failed");
            return null;
        }
    }

    public String encryptByAes(String rawData) {
        return encryptByAes(rawData, 1);
    }

    public String encryptByAes(String rawData, int keyId) {
        if (StringUtils.isEmpty(rawData)) {
            LOG.warn("the raw data is empty");
            return null;
        }
        try {
            /*HashMap<Integer, String> workKeys = init();
            if (workKeys.isEmpty() || StringUtils.isEmpty(workKeys.get(keyId))) {
                LOG.error("work key used to encrypt is empty");
                return null;
            }*/
            String workKey = FileUtils.getKey(FileUtils.WORK_FILE_NAME);
            if (workKey == null) {
                workKey=genencryptWorkKey();
                FileUtils.saveKey(workKey,FileUtils.WORK_FILE_NAME);
            }
            byte[] decryptWorkKey = decryptWorkKey(workKey);
            if (decryptWorkKey == null) {
                throw new IllegalArgumentException("work key decrypt error");
            }
            String keyAlgorithm = cipherConfig.getAes().getKey().getAlgorithm();
            SecretKeySpec secretKeySpec = new SecretKeySpec(decryptWorkKey, keyAlgorithm);
            byte[] iv = CipherUtil.initIV();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            String algorithm = cipherConfig.getAes().getAlgorithm();
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encryptedData = cipher.doFinal(rawData.getBytes());
            String striv = CipherUtil.parseByte2Hexstr(iv);
            String strData = CipherUtil.parseByte2Hexstr(encryptedData);
            String delimiter = cipherConfig.getDelimiter();
            return String.valueOf(keyId) + delimiter + striv + delimiter + strData;
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException
            | BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException e) {
            LOG.error("encrypt rawdata error");
            return null;
        }
    }

    public String decryptByAes(String rawData) {
        if (StringUtils.isEmpty(rawData)) {
            LOG.warn("the raw data is empty");
            return null;
        }
        try {
            String delimiter = cipherConfig.getDelimiter();
            String[] array = StringUtils.delimitedListToStringArray(rawData, delimiter);
            if (ARRAT_LENGTH_3 != array.length) {
                LOG.error("delimite rawdata length is not three");
                throw new IllegalArgumentException("rawData illegal");
            }
            /*HashMap<Integer, String> workKeys = init();
            String workkey = workKeys.get(Integer.valueOf(array[0]));
            if (StringUtils.isEmpty(workkey)) {
                LOG.error("work key used to decrypt is empty");
                return null;
            }*/
            String workKey = FileUtils.getKey(FileUtils.WORK_FILE_NAME);
            if (workKey == null) {
                workKey=genencryptWorkKey();
                FileUtils.saveKey(workKey,FileUtils.WORK_FILE_NAME);
            }
            byte[] decryptworkkey = decryptWorkKey(workKey);
            if (decryptworkkey == null) {
                throw new IllegalArgumentException("work key decrypt error");
            }
            String keyAlgorithm = cipherConfig.getAes().getKey().getAlgorithm();
            SecretKeySpec secretKeySpec = new SecretKeySpec(decryptworkkey, keyAlgorithm);
            byte[] iv = CipherUtil.parseHexstr2Byte(array[1]);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            String algorithm = cipherConfig.getAes().getAlgorithm();
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] decryptData = cipher.doFinal(CipherUtil.parseHexstr2Byte(array[ARRAT_LENGTH_2]));
            return new String(decryptData);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException
            | BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException e) {
            LOG.error("decrypt error");
            return null;
        }
    }

    public static String getSafeRandomToString(int num) throws NoSuchAlgorithmException {
        return toHex(getSafeRandom(num));
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
