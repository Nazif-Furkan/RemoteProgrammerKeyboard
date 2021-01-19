package com.naziffurkandemirsoy.programmerkeyboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    String IpAddress ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.csharp_keyboard);
        setTitle("C# KEYBOARD");

        //ServerIpFinder.Find();

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        IpAddress =  settings.getString("UserIpAddress","192.168.1.33");
        GetButtons(IpAddress);
    }

    void StartSettingsActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();

    }

    void GetButtons(String ipAddress)  {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://"+ ipAddress+ ":5001/api/Keys/CSharp";

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        for(int i=0;i<jsonArray.length();i++){

                            JSONObject jresponse =
                                    null;
                            try {
                                jresponse = jsonArray.getJSONObject(i);

                                CreateButton(jresponse,ipAddress);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        Log.println(Log.DEBUG,"appfurkan",error.toString());
                        StartSettingsActivity();


                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    void CreateButton(JSONObject jresponse, String ipAddress) throws JSONException {
        Button myButton = new Button(this);
        Log.println(Log.DEBUG,"appfurkan","debene" + jresponse.getString("id"));
        com.google.android.flexbox.FlexboxLayout buttonLayout = (com.google.android.flexbox.FlexboxLayout)findViewById(R.id.buttonLayout);

        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        Resources r = this.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10,
                r.getDisplayMetrics()
        );

        lp.setMargins(px,px,px,px);
        buttonLayout.addView(myButton,lp);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        myButton.setText( jresponse.getString("key"));
        myButton.setGravity(Gravity.CENTER);
        String api = jresponse.getString("id");

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://"+ ipAddress+ ":5001/api/CSharpKeyboard/" + api;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, response -> {
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                            Log.println(Log.DEBUG,"appfurkan",response.toString());
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                                Log.println(Log.DEBUG,"appfurkan",error.toString());
                            }
                        });
                requestQueue.add(jsonObjectRequest);

            }
        });

    }

}