package com.huawei.dmestore.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utils to encrypt/decrypt eSight password string
 */

public class CipherUtils {

    private AESCipher aesCipher;

    private static final Logger LOGGER = LoggerFactory.getLogger(CipherUtils.class);


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


    public  String decryptString(String sSrc) {
        //return aesDncode(sSrc, multitypeutil);
        return aesCipher.decryptByAES(sSrc);
    }


}
