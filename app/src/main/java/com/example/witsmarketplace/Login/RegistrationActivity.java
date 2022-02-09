package com.example.witsmarketplace.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.Output;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.witsmarketplace.LandingPage.LandingPage;
import com.example.witsmarketplace.R;
import com.example.witsmarketplace.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Year;

public class RegistrationActivity extends AppCompatActivity {

    private static Context context;
    DatePickerDialog datePickerDialog ;
    public static SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        sharedPreference = new SharedPreference(this);

        setContentView(R.layout.activity_registration);
        final EditText InputFirstname = findViewById(R.id.Firstname);
        final EditText InputLastname = findViewById(R.id.Lastname);
        final EditText InputEmail = findViewById(R.id.email);
        final EditText InputDate = findViewById(R.id.date);
        final EditText InputPassword = findViewById(R.id.Password);
        final EditText InputConPass = findViewById(R.id.ConfirmPassword);
        Button InputButton = findViewById(R.id.SignUp);
        TextView login = findViewById(R.id.goToLoginPage);

        InputDate.setOnClickListener(new  OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int Month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        InputDate.setText(year + "-" + (Month +1)+ "-" + day);
                    }
                }, year, Month, day);
                datePickerDialog.show();
            }
        });

        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                finish();
            }
        });

        InputButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstname = InputFirstname.getText().toString().trim();
                String Lastname = InputLastname.getText().toString().trim();
                String email = InputEmail.getText().toString().trim();
                String date = InputDate.getText().toString().trim();
                String Password = InputPassword.getText().toString().trim();
                String ConfirmPassword = InputConPass.getText().toString().trim();

                if (firstname.isEmpty()) {
                    InputFirstname.setError("input required");
                }
                else if (Lastname.isEmpty()) {
                    InputLastname.setError("input required");
                }
                else if (email.isEmpty()) {
                    InputEmail.setError("input required");
                }
                else if (date.isEmpty()) {
                    InputDate.setError("input required");
                }
                else if (Password.isEmpty()) {
                    InputPassword.setError("input required");
                }
                else if (ConfirmPassword.isEmpty()) {
                    InputConPass.setError("input required");
                }
                else if (!ConfirmPassword.equals(Password)) {
                    InputConPass.setError("password doesn't match");
                }
                else{
                    register(context , firstname, Lastname, email, date, Password);
                }
            }
        });
    }


    public static void register(final Context context, String firstname, String Lastname, String email, String date, String Password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstname", firstname);
        contentValues.put("lastname", Lastname);
        contentValues.put("email", email);
        contentValues.put("dateOfBirth", date);
        contentValues.put("password", Password);

        new ServerCommunicator("https://lamp.ms.wits.ac.za/home/s2172765/market_place_app_register.php", contentValues) {
            @Override
            protected void onPreExecute() {}
            @Override
            protected void onPostExecute(String output) {
                try {
                    JSONArray users = new JSONArray(output);
                    JSONObject object = users.getJSONObject(0);

                    String status = object.getString("register_status");
                    String message = object.getString("status_message");

                    if(status.equals("1")){
                        sharedPreference.setSH("email", email);

                        Intent intent = new Intent(context, LandingPage.class);
                        context.startActivity(intent);
                        ((RegistrationActivity)context).finish();
                        Toast.makeText(context ,"You have Succesfully registered",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(context, message , Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}

