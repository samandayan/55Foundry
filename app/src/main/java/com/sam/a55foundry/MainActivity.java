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

import static com.sam.a55foundry.RSAKeyPairGenerator.PRIVATE_KEY_TAG;
import static com.sam.a55foundry.RSAKeyPairGenerator.PUBLIC_KEY_TAG;

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

        bindViews();

        rsaUtil = new RSAUtil(this);
    }

    /**
     * This method find references to all the
     * views for later processing and modifications.
     */
    public void bindViews() {
        generateKeyPair = findViewById(R.id.generate_key_pair);
        textViewer = findViewById(R.id.text_viewer);
        text_to_encrypt = findViewById(R.id.text_to_encrypt);
        view_public_key = findViewById(R.id.view_public_key);
        view_private_key = findViewById(R.id.view_private_key);
    }

    /**
     * Per project requirement this is added here
     * so that after the App is restarted the
     * Generate Key Pair button gets disabled.
     */
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String savedPublicKey = prefs.getString(PUBLIC_KEY_TAG, null);
        String savedPrivateKey = prefs.getString(PRIVATE_KEY_TAG, null);

        if (savedPublicKey != null && savedPrivateKey != null) {
            generateKeyPair.setEnabled(false);
            generateKeyPair.setBackground(getDrawable(R.drawable.button_disabled));
            view_private_key.setBackground(getDrawable(R.drawable.button_enabled));
            view_public_key.setBackground(getDrawable(R.drawable.button_enabled));
        }
    }

    /**
     *
     * @param view The Generate Key Pair Button
     *             This method generates the
     *             RSA Key Pair.
     */
    public void generate_key_pair(View view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String savedPublicKey = prefs.getString(PUBLIC_KEY_TAG, null);
        String savedPrivateKey = prefs.getString(PRIVATE_KEY_TAG, null);

        try {
            if (savedPublicKey == null && savedPrivateKey == null) {
                rsaUtil.generateKeyPair();
                view_private_key.setBackground(getDrawable(R.drawable.button_enabled));
                view_public_key.setBackground(getDrawable(R.drawable.button_enabled));
            }
        } catch (Exception e) {
            Log.i("Error_Message", e.getMessage());
        }
    }

    /**
     *
     * @param view The View Private Key Button
     *             This button displays the
     *             private key.
     */
    public void view_private_key(View view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String savedPrivateKey = prefs.getString(PRIVATE_KEY_TAG, null);

        if (savedPrivateKey != null) {
            textViewer.setText("-----BEGIN PRIVATE KEY-----\n");
            textViewer.append(savedPrivateKey);
            textViewer.append("\n-----END PRIVATE KEY-----");
        }
    }

    /**
     *
     * @param view The View Public Key Button
     *             This button displays the
     *             public key.
     */
    public void view_public_key(View view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String savedPrivateKey = prefs.getString(PUBLIC_KEY_TAG, null);

        if (savedPrivateKey != null) {
            textViewer.setText("-----BEGIN PUBLIC KEY-----\n");
            textViewer.append(savedPrivateKey);
            textViewer.append("\n-----END PUBLIC KEY-----");
        }
    }

    /**
     *
     * @param view The ImageView that clear text to encrypt
     *             This is the icon that sits to the right
     *             of text to encrypt EditText. Upon
     *             clicking this button the encrypt EditText
     *             will be cleared of its textual contents.
     */
    public void clear_text_to_encrypt(View view) {
        text_to_encrypt.setText("");
    }

    /**
     *
     * @param view Encrypt Text Button
     *             This button will have
     *             the text encrypted and
     *             will then display the
     *             encrypted text.
     */
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