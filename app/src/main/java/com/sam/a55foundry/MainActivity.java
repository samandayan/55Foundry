package com.sam.a55foundry;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    RSAUtil rsaUtil;

    Button generateKeyPair;
    Button view_public_key;
    Button view_private_key;
    EditText textViewer;
    EditText text_to_encrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateKeyPair = findViewById(R.id.generate_key_pair);
        textViewer = findViewById(R.id.text_viewer);
        text_to_encrypt = findViewById(R.id.text_to_encrypt);
        view_public_key = findViewById(R.id.view_public_key);
        view_private_key = findViewById(R.id.view_private_key);

        rsaUtil = new RSAUtil(this);

        /*try {
            RSAUtil rsaUtil = new RSAUtil(this);
            rsaUtil.main(null);
        } catch (Exception e) {
            Log.i("Error_Message", e.getMessage());
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String savedPublicKey = prefs.getString("PUBLIC_KEY", null);
        String savedPrivateKey = prefs.getString("PRIVATE_KEY", null);

        if (savedPublicKey != null && savedPrivateKey != null) {
            generateKeyPair.setEnabled(false);
            generateKeyPair.setBackground(getDrawable(R.drawable.button_disabled));
            view_private_key.setBackground(getDrawable(R.drawable.button_enabled));
            view_public_key.setBackground(getDrawable(R.drawable.button_enabled));
        }
    }

    public void generate_key_pair(View view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String savedPublicKey = prefs.getString("PUBLIC_KEY", null);
        String savedPrivateKey = prefs.getString("PRIVATE_KEY", null);

        try {
            if (savedPublicKey == null && savedPrivateKey == null)
                rsaUtil.generateKeyPair();
        } catch (Exception e) {
            Log.i("Error_Message", e.getMessage());
        }
    }

    public void view_private_key(View view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String savedPrivateKey = prefs.getString("PRIVATE_KEY", null);

        if (savedPrivateKey != null) {
            textViewer.setText("-----BEGIN PRIVATE KEY-----\n");
            textViewer.append(savedPrivateKey);
            textViewer.append("\n-----END PUBLIC KEY-----");
        }
    }

    public void view_public_key(View view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String savedPrivateKey = prefs.getString("PUBLIC_KEY", null);

        if (savedPrivateKey != null) {
            textViewer.setText("-----BEGIN PUBLIC KEY-----\n");
            textViewer.append(savedPrivateKey);
            textViewer.append("\n-----END PUBLIC KEY-----");
        }
    }

    public void clear_text_to_encrypt(View view) {
        text_to_encrypt.setText("");
    }

    public void perform_encryption(View view) {
        String encrypted_text = null;
        try {
            if (!TextUtils.isEmpty(text_to_encrypt.getText().toString()))
                encrypted_text = rsaUtil.main(text_to_encrypt.getText().toString());

            if (!TextUtils.isEmpty(encrypted_text))
                textViewer.setText(encrypted_text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}