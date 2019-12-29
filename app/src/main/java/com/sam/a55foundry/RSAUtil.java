package com.sam.a55foundry;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtil {

    Context context;
    RSAKeyPairGenerator rsaKeyPairGenerator;

    public RSAUtil(Context context) {
        this.context = context;
    }

    public void generateKeyPair() {
        try {
            rsaKeyPairGenerator = new RSAKeyPairGenerator();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();

            String b64PublicKey = Base64.getEncoder().encodeToString(rsaKeyPairGenerator.getPublicKey().getEncoded());
            editor.putString("PUBLIC_KEY", b64PublicKey);

            String b64PrivateKey = Base64.getEncoder().encodeToString(rsaKeyPairGenerator.getPrivateKey().getEncoded());
            editor.putString("PRIVATE_KEY", b64PrivateKey);
            editor.commit();


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public  PublicKey getPublicKey() throws Exception{
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String savedPublicKey = prefs.getString("PUBLIC_KEY", null);
        String b64PublicKey = savedPublicKey;


        byte[] publicBytes = Base64.getDecoder().decode(b64PublicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);


        Log.i("Cool", b64PublicKey);
        return pubKey;
    }

    public  PrivateKey getPrivateKey() throws Exception{
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String savedPrivateKey = prefs.getString("PRIVATE_KEY", null);
        String b64PrivateKey = savedPrivateKey;

        byte[] privateBytes = Base64.getDecoder().decode(b64PrivateKey);


        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privKey = kf.generatePrivate(keySpec);
        Log.i("Cool", b64PrivateKey);

        return privKey;
    }

    public  byte[] encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
        return cipher.doFinal(data.getBytes());
    }

    public  String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }

    public  String decrypt(String data) throws Exception {
        return decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey());
    }

    public String main(String textToEncrypt) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, BadPaddingException {
        try {
            String encryptedString = Base64.getEncoder().encodeToString(encrypt(textToEncrypt));
            Log.i("asdf", encryptedString);
            String decryptedString = decrypt(encryptedString);
            Log.i("asdf", decryptedString);
            return encryptedString;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}