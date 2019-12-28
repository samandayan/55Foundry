package com.sam.a55foundry;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class RSAKeyPairGenerator {

    static KeyPairGenerator kpg;

    static {
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
        } catch (Exception e) {

        }
    }

    private static KeyPair getKeyPair() {
        return kpg.generateKeyPair();
    }

    public static String getPublicKey() {
        return Base64.getMimeEncoder().encodeToString(getKeyPair().getPublic().getEncoded());
    }

    public static String getPrivateKey() {
        return Base64.getMimeEncoder().encodeToString(getKeyPair().getPrivate().getEncoded());
    }

    public static String getAlgorithm() {
        return kpg.getAlgorithm();
    }
}