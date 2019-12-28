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
        try {
            rsaKeyPairGenerator = new RSAKeyPairGenerator();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB";
    private static String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKAUZV+tjiNBKhlBZbKBnzeugpdYPhh5PbHanjV0aQ+LF7vetPYhbTiCVqA3a+Chmge44+prlqd3qQCYra6OYIe7oPVq4mETa1c/7IuSlKJgxC5wMqYKxYydb1eULkrs5IvvtNddx+9O/JlyM5sTPosgFHOzr4WqkVtQ71IkR+HrAgMBAAECgYAkQLo8kteP0GAyXAcmCAkA2Tql/8wASuTX9ITD4lsws/VqDKO64hMUKyBnJGX/91kkypCDNF5oCsdxZSJgV8owViYWZPnbvEcNqLtqgs7nj1UHuX9S5yYIPGN/mHL6OJJ7sosOd6rqdpg6JRRkAKUV+tmN/7Gh0+GFXM+ug6mgwQJBAO9/+CWpCAVoGxCA+YsTMb82fTOmGYMkZOAfQsvIV2v6DC8eJrSa+c0yCOTa3tirlCkhBfB08f8U2iEPS+Gu3bECQQCrG7O0gYmFL2RX1O+37ovyyHTbst4s4xbLW4jLzbSoimL235lCdIC+fllEEP96wPAiqo6dzmdH8KsGmVozsVRbAkB0ME8AZjp/9Pt8TDXD5LHzo8mlruUdnCBcIo5TMoRG2+3hRe1dHPonNCjgbdZCoyqjsWOiPfnQ2Brigvs7J4xhAkBGRiZUKC92x7QKbqXVgN9xYuq7oIanIM0nz/wq190uq0dh5Qtow7hshC/dSK3kmIEHe8z++tpoLWvQVgM538apAkBoSNfaTkDZhFavuiVl6L8cWCoDcJBItip8wKQhXwHp0O3HLg10OEd14M58ooNfpgt+8D8/8/2OOFaR0HzA+2Dm";

    public  PublicKey getPublicKey() throws Exception{
        String b64PublicKey = Base64.getEncoder().encodeToString(rsaKeyPairGenerator.getPublicKey().getEncoded());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String savedPublicKey = prefs.getString("PUBLIC_KEY", null);

        if (savedPublicKey == null) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("PUBLIC_KEY", b64PublicKey);
            editor.commit();
        } else {
            b64PublicKey = savedPublicKey;
        }

        byte[] publicBytes = Base64.getDecoder().decode(b64PublicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);


        Log.i("Cool", b64PublicKey);
        return pubKey;
    }

    public  PrivateKey getPrivateKey() throws Exception{
        String b64PrivateKey = Base64.getEncoder().encodeToString(rsaKeyPairGenerator.getPrivateKey().getEncoded());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String savedPrivateKey = prefs.getString("PRIVATE_KEY", null);

        if (savedPrivateKey == null) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("PRIVATE_KEY", b64PrivateKey);
            editor.commit();
        } else {
            b64PrivateKey = savedPrivateKey;
        }

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

    public  void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, BadPaddingException {
        try {
            String encryptedString = Base64.getEncoder().encodeToString(encrypt("This is Sam Dayan"));
            Log.i("asdf", encryptedString);
            String decryptedString = decrypt(encryptedString);
            Log.i("asdf", decryptedString);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}