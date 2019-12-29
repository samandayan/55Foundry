package com.sam.a55foundry;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static com.sam.a55foundry.RSAKeyPairGenerator.PRIVATE_KEY_TAG;
import static com.sam.a55foundry.RSAKeyPairGenerator.PUBLIC_KEY_TAG;

public class RSAUtil {

    public static final String decryptedTag = "decrypted";

    Context context;
    RSAKeyPairGenerator rsaKeyPairGenerator;

    /**
     *
     * @param context The context using which
     *                SharedPreferences will
     *                be retrieved or added.
     */
    public RSAUtil(Context context) {
        this.context = context;
    }

    /**
     * This method generates the Key Pair.
     * The Key Pair is the public and private
     * keys that will be used for encryption
     * and decryption.
     */
    public void generateKeyPair() {
        try {
            rsaKeyPairGenerator = new RSAKeyPairGenerator();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();

            String b64PublicKey = Base64.getEncoder().encodeToString(rsaKeyPairGenerator.getPublicKey().getEncoded());
            editor.putString(PUBLIC_KEY_TAG, b64PublicKey);

            String b64PrivateKey = Base64.getEncoder().encodeToString(rsaKeyPairGenerator.getPrivateKey().getEncoded());
            editor.putString(PRIVATE_KEY_TAG, b64PrivateKey);
            editor.commit();


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return This method will return the Public Key
     * @throws Exception
     * This method will check to see if the public key
     * is present is shared preferences, and if it is
     * the Public Key will be generated from the
     * SharedPreferences and returned. If the public key
     * is not present then it will be generated and saved
     * into sharedpreferences for later retrieval and usage.
     */
    public  PublicKey getPublicKey() throws Exception{
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String savedPublicKey = prefs.getString(PUBLIC_KEY_TAG, null);
        String b64PublicKey = savedPublicKey;


        byte[] publicBytes = Base64.getDecoder().decode(b64PublicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        return pubKey;
    }

    /**
     *
     * @return This method will return the Private Key
     * @throws Exception
     * This method will check to see if the private key
     * is present is shared preferences, and if it is
     * the private Key will be generated from the
     * SharedPreferences and returned. If the private key
     * is not present then it will be generated and saved
     * into sharedpreferences for later retrieval and usage.
     */
    public  PrivateKey getPrivateKey() throws Exception{
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String savedPrivateKey = prefs.getString(PRIVATE_KEY_TAG, null);
        String b64PrivateKey = savedPrivateKey;

        byte[] privateBytes = Base64.getDecoder().decode(b64PrivateKey);


        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privKey = kf.generatePrivate(keySpec);
        Log.i("Cool", b64PrivateKey);

        return privKey;
    }

    /**
     * Encryption of information is performed here.
     * @param data This is the text to encrypt
     * @return This is the encrypted data.
     * @throws Exception
     */
    public  byte[] encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
        return cipher.doFinal(data.getBytes());
    }

    /**
     * Decryption of information is performed here.
     * @param data This is the text to decrypt.
     * @param privateKey This is the private key to decrypt data.
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public  String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }

    /**
     * Decryption of information is performed here.
     * @param data This is the text to decrypt.
     * @return
     * @throws Exception
     */
    public  String decrypt(String data) throws Exception {
        return decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey());
    }

    /**
     *
     * @param textToEncrypt This is the text to be encrypted.
     * @return This method returns the textToEncrypt in
     * encrypted format. After encryption is done for
     * confirmation of correct encryption and decryption
     * the decryptedTag may be used to view decrypted text.
     */
    public String main(String textToEncrypt) {
        try {
            String encryptedString = Base64.getEncoder().encodeToString(encrypt(textToEncrypt));
            String decryptedString = decrypt(encryptedString);
            Log.i(decryptedTag, decryptedString);
            return encryptedString;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}