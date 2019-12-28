package com.sam.a55foundry;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    RSAUtil rsaUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            findViewById(R.id.generate_key_pair).setEnabled(false);
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
}
