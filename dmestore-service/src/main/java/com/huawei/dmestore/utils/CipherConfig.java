package com.huawei.dmestore.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


public class CipherConfig {
    private RootKey rootKey=new RootKey();
    private Aes aes=new Aes();
    private Pbkdf2 pbkdf2=new Pbkdf2();
    private String delimiter;

    public Aes getAes() {
        return aes;
    }

    public void setAes(Aes aes) {
        this.aes = aes;
    }

    public Pbkdf2 getPbkdf2() {
        return pbkdf2;
    }

    public void setPbkdf2(Pbkdf2 pbkdf2) {
        this.pbkdf2 = pbkdf2;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public RootKey getRootKey() {
        return rootKey;
    }

    public void setRootKey(RootKey rootKey) {
        this.rootKey = rootKey;
    }

    public static class RootKey{
        private String key;
        private String algorithm;
        private String salt;
        private String iteration;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getAlgorithm() {
            return algorithm;
        }

        public void setAlgorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        public String getIteration() {
            return iteration;
        }

        public void setIteration(String iteration) {
            this.iteration = iteration;
        }
    }

    public static class Aes {
        private String workKey;
        private String algorithm;
        private Key key;

        public String getWorkKey() {
            return workKey;
        }

        public void setWorkKey(String workKey) {
            this.workKey = workKey;
        }

        public String getAlgorithm() {
            return algorithm;
        }

        public void setAlgorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        public Key getKey() {
            return key;
        }

        public void setKey(Key key) {
            this.key = key;
        }

        public static class Key{
            private String algorithm;

            public String getAlgorithm() {
                return algorithm;
            }

            public void setAlgorithm(String algorithm) {
                this.algorithm = algorithm;
            }
        }
    }

    public static class Pbkdf2 {
        private String iteration;
        private String length;

        public String getIteration() {
            return iteration;
        }

        public void setIteration(String iteration) {
            this.iteration = iteration;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }
    }

}
