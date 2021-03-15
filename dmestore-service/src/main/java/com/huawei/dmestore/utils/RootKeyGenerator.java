package com.huawei.dmestore.utils;

import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


public class RootKeyGenerator {

    private CipherConfig cipherConfig;

    public CipherConfig getCipherConfig() {
        return cipherConfig;
    }

    public void setCipherConfig(CipherConfig cipherConfig) {
        this.cipherConfig = cipherConfig;
    }

    private static final Logger LOG= LoggerFactory.getLogger(RootKeyGenerator.class);
    private static final byte[] CODED_COMPONENT={10,21,30,69,85,120,21,17,73,121,11,2,3,69,85,100,21,33,76,12,17,63,25,53,69,8,69,49,73,13,11,40};

    protected  byte[] generateRootKey(){
        int length=CODED_COMPONENT.length;
        //byte[] rootKey=CipherUtil.parseHexstr2Byte(System.getProperty("cipher.rootKey.key"));
        //System.out.println("rootkey="+cipherConfig.getRootKey().getKey());
        byte[] rootKey=CipherUtil.parseHexstr2Byte(cipherConfig.getRootKey().getKey());
        if(null==rootKey||length!=rootKey.length)
        {
            throw new IllegalArgumentException("ROOT KEY Illegeal");
        }
        byte[] modResult= new byte[length];
        for (int i=0;i<length;i++){
            modResult[i]= (byte) (CODED_COMPONENT[i]^rootKey[i]);
        }
        //String rootAlgorithm=System.getProperty("cipher.rootKey.algorithm");
        String rootAlgorithm=cipherConfig.getRootKey().getAlgorithm();
        if("PBKDF2".equalsIgnoreCase(rootAlgorithm)){
            String key=CipherUtil.parseByte2Hexstr(modResult);
            //byte[] rootsalt=CipherUtil.parseHexstr2Byte(System.getProperty("cipher.rootKey.salt"));
            byte[] rootsalt=CipherUtil.parseHexstr2Byte(cipherConfig.getRootKey().getSalt());
            //Integer rootIteration=Integer.valueOf(System.getProperty("cipher.rootKey.iteration"));
            Integer rootIteration=Integer.valueOf(cipherConfig.getRootKey().getIteration());
            return encryptRootKey(key,rootsalt,rootIteration);
        }
        return modResult;
    }

    private  byte[] encryptRootKey(String key,byte[] salt,int iterations){
        if (StringUtils.isEmpty(key)|| iterations<10000){
            LOG.error("the parames use to encrypt by pbkdf2 is illegal");
            throw new IllegalArgumentException("params illegal");
        }
        //Integer length=Integer.valueOf(System.getProperty("cipher.pbkdf2.length"));
        Integer length=Integer.valueOf(cipherConfig.getPbkdf2().getLength());
        PKCS5S2ParametersGenerator generator=new PKCS5S2ParametersGenerator(new SHA256Digest());
        generator.init(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(key.toCharArray()),salt,iterations);
        KeyParameter parameter= (KeyParameter) generator.generateDerivedMacParameters(length);
        return parameter.getKey();
    }
}
