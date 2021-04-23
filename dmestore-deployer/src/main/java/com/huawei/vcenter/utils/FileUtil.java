package com.huawei.vcenter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class FileUtil {
    public static final String KEYSTORE_PASS_NAME = "keystorepass.txt";
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
    private static void createFile(File file) {
        // 判断文件是否存在
        if (!file.exists()) {
            LOGGER.info("key file not exists, create it ...");
            try {
                boolean re = file.createNewFile();
            } catch (IOException e) {
                LOGGER.error("Failed to create file " + file.getName());
            }
        }
    }

    public static String getKey(String fileName) {
        File file = new File(fileName);
        createFile(file);
        String line = null;
        StringBuffer result = new StringBuffer();
        FileInputStream f = null;
        InputStreamReader in = null;
        BufferedReader br = null;
        try {
            f = new FileInputStream(file);
            in = new InputStreamReader(f, "utf-8");
            br = new BufferedReader(in);
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            if (result.length() < 1) {
                return null;
            }
            return result.toString();
        } catch (IOException e) {
            LOGGER.error("Failed to get key: " + fileName);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
            if (f != null) {
                try {
                    f.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
        return null;
    }

    public static void saveKey(String key, String fileName) {
        File file = new File(fileName);
        createFile(file);
        FileOutputStream f = null;
        OutputStreamWriter out = null;
        BufferedWriter bw = null;
        try {
            f = new FileOutputStream(file, false);
            out = new OutputStreamWriter(f, "utf-8");
            bw = new BufferedWriter(out);
            bw.write(key);
        } catch (IOException e) {
            LOGGER.error("Cannot save key: " + fileName);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
            if (f != null) {
                try {
                    f.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
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
}
