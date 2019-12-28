package com.sam.a55foundry;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    RSAUtil rsaUtil;

    Button generateKeyPair;
    EditText textViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateKeyPair = findViewById(R.id.generate_key_pair);
        textViewer = findViewById(R.id.text_viewer);

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
        }
    }

    public void generate_key_pair(View view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String savedPublicKey = prefs.getString("PUBLIC_KEY", null);
        String savedPrivateKey = prefs.getString("PRIVATE_KEY", null);

        try {
            if (savedPublicKey == null && savedPrivateKey == null)
                rsaUtil.generateKeyPair();

            rsaUtil.main(null);
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
}
