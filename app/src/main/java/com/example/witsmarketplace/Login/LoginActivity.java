package com.example.witsmarketplace.Login;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.witsmarketplace.LandingPage.LandingPage;
import com.example.witsmarketplace.R;
import com.example.witsmarketplace.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText userName;
    private EditText userPassword;
    private Button loginBtn;
    private static Context context;
    private static String status,statusMsg;
    public static SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        sharedPreference = new SharedPreference(this);

        TextView txtview= findViewById(R.id.signupbtn);
        String text = "Don't have an account? Sign Up here";
        SpannableString ss = new SpannableString(text);
        ForegroundColorSpan fs = new ForegroundColorSpan(Color.YELLOW);

        txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RegistrationActivity.class);
                context.startActivity(intent);
                finish();
            }
        });

        ss.setSpan(fs,22,35,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtview.setText(ss);
        txtview.setMovementMethod(LinkMovementMethod.getInstance());

        loginBtn = findViewById(R.id.login_btn);
        userName = findViewById(R.id.etUsername);
        userPassword = findViewById(R.id.etPassword);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameinput = userName.getText().toString().trim();
                String userPasswordInput = userPassword.getText().toString().trim();

                if(userNameinput.isEmpty()){
                    userName.setError("Required");
                    Toast.makeText(LoginActivity.this,"Incomplete fields",Toast.LENGTH_SHORT).show();
                }
                else if(userPasswordInput.isEmpty()){
                    userPassword.setError("Required");
                    Toast.makeText(LoginActivity.this,"Incomplete fields",Toast.LENGTH_SHORT).show();
                }
                else{
                    ServerLogin(context,userNameinput,userPasswordInput);
                }
            }
        });
    }

    private static void ServerLogin(final Context c, final String email, String password)
    {
        ContentValues cv = new ContentValues();
        cv.put("email",email);
        cv.put("password",password);

        new ServerCommunicator("https://lamp.ms.wits.ac.za/home/s2172765/market_place_app_login.php", cv) {
            @Override
            protected void onPreExecute(){
            };
            @Override
            protected void onPostExecute(String output) {
                try {
                    JSONArray users = new JSONArray(output);
                    JSONObject object_one = users.getJSONObject(0);

                    String mstatus = object_one.getString("login_status");
                    statusMsg = object_one.getString("login_message");

                    Toast.makeText(context,statusMsg,Toast.LENGTH_LONG).show();

                    int statusNum = Integer.parseInt(mstatus);

                    if (statusNum == 1){
                        sharedPreference.setSH("email", email);
                        Intent intent = new Intent(context, LandingPage.class);
                        context.startActivity(intent);
                        ((LoginActivity)context).finish();
                    }
                }
                catch(JSONException e){

                    e.printStackTrace();
                }
            }
        }.execute();
    }
}