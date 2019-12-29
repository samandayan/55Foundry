package com.sam.a55foundry;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * This class generates the RSA Key Pair.
 * The key pair is the public and private
 * key which are used for encryption and
 * decryption.
 */
public class RSAKeyPairGenerator {

    public static final String PUBLIC_KEY_TAG = "PUBLIC_KEY";
    public static final String PRIVATE_KEY_TAG = "PRIVATE_KEY";

    private PrivateKey privateKey;
    private PublicKey publicKey;

    /**
     * This constructor generates the
     * public and private keys.
     * @throws NoSuchAlgorithmException
     */
    public RSAKeyPairGenerator() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    /**
     * This method retrieves the private key.
     * @return The private key.
     */
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * This method retrieves the public key.
     * @return The public key.
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }
}