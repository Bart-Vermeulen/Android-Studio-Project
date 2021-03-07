package com.example.project_1_vermeulen_barthelemy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executor;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button access = null;
    EditText iban = null;
    TextView account_name = null;
    TextView amount = null;
    TextView currency = null;
    ArrayList<String> theIBANS = new ArrayList<String>();
    ArrayList<String> theNames = new ArrayList<String>();
    ArrayList<String> theAmounts = new ArrayList<String>();
    ArrayList<String> theCurrencies = new ArrayList<String>();

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_main);

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
                        System.exit(0);
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();

            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

        biometricPrompt.authenticate(promptInfo);


        access = (Button) findViewById(R.id.button);
        iban = (EditText) findViewById(R.id.editTextTextPersonName);
        account_name = (TextView) findViewById(R.id.textView6);
        amount = (TextView) findViewById(R.id.textView7);
        currency = (TextView) findViewById(R.id.textView8);

        access.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        String clientIBAN = iban.getText().toString();

        String url = "https://6007f1a4309f8b0017ee5022.mockapi.io/api/m1/accounts";
        RequestQueue mQueue = Volley.newRequestQueue(this);


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray jsonArray = response;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject theAccount = jsonArray.getJSONObject(i);
                                String theIBAN = theAccount.getString("iban");
                                String theName = theAccount.getString("account_name");
                                String theAmount = theAccount.getString("amount");
                                String theCurrency = theAccount.getString("currency");
                                theIBANS.add(theIBAN);
                                theNames.add(theName);
                                theAmounts.add(theAmount);
                                theCurrencies.add(theCurrency);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        boolean isIN = false;
        int number = 0;

        for (int i=0; i <  theIBANS.size(); i++){
            if (theIBANS.get(i).equals(clientIBAN)){
                number = i;
                isIN = true;
            }
        }

        if (isIN){
            account_name.setText(theNames.get(number));
            amount.setText(theAmounts.get(number));
            currency.setText(theCurrencies.get(number));
        }
        else{
            Toast.makeText(getApplicationContext(),"IBAN incorrect", Toast.LENGTH_LONG).show();
        }
        mQueue.add(request);


    }
}