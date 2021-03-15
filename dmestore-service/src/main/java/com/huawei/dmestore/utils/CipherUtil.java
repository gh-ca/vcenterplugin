package com.huawei.dmestore.utils;

import java.security.SecureRandom;

public class CipherUtil {

    protected  static byte[] initIV(){
        byte[] iv=new byte[16];
        SecureRandom secureRandom=new SecureRandom();
        secureRandom.nextBytes(iv);
        return iv;
    }
    public static byte[] parseHexstr2Byte(String hexString){
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

        }
        return d;
    }
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String parseByte2Hexstr(byte[] src){
        //String hs = "";
        StringBuffer sb = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < src.length; n++) {
            stmp = (Integer.toHexString(src[n] & 0XFF));
            if (stmp.length() == 1)
            {
                sb.append( "0" + stmp);
            }
                //hs = hs + "0" + stmp;

            else
            {
                sb.append(stmp);
            }
                //hs = hs + stmp;

        }
        return sb.toString().toUpperCase();
    }
}
