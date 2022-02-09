package com.example.witsmarketplace.ProfilePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.witsmarketplace.Account;
import com.example.witsmarketplace.R;
import com.example.witsmarketplace.SharedPreference;
import com.example.witsmarketplace.fave_cart.FavItem;
import com.example.witsmarketplace.fave_cart.cart;
import com.example.witsmarketplace.fave_cart.favorite;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity {

    String webURL = "https://lamp.ms.wits.ac.za/home/s2172765/app_fetch_profile.php?USER_EMAIL="; // id == email
    public static SharedPreference sharedPreference;
    TextView name;
    TextView Balance;
    TextView Email;
    TextView date;
    TextView userId;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreference = new SharedPreference(this);
        String userEmail = sharedPreference.getSH("email");
        //Bottom Navigation
        BottomNavigationView bnv = findViewById(R.id.bottom_navigation);
        bnv.setOnNavigationItemSelectedListener(navListener);
        bnv.getMenu().getItem(3).setChecked(true);

        name = (TextView)findViewById(R.id.tv_name);
        Balance = (TextView) findViewById(R.id.profileBalance);
        Email= (TextView) findViewById(R.id.profileEmail);
        date = (TextView) findViewById(R.id.profileDate);
        userId = (TextView) findViewById(R.id.profileID);

        requestQueue = Volley.newRequestQueue(this);
        renderItems(userEmail);
    }

    //    Bottom Navigation
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent intent = null;
                    if (item.getItemId() == R.id.nav_cart){
                        intent = new Intent(getApplicationContext(), cart.class);
                        startActivity(intent);
                    }
                    else if (item.getItemId() == R.id.nav_favorite) {
                        intent = new Intent(getApplicationContext(), favorite.class);
                        startActivity(intent);
                    }
                    else if(item.getItemId() == R.id.nav_account){
                        intent = new Intent(getApplicationContext(), Account.class);
                        startActivity(intent);
                    }
                    return true;
                }
            };


    //  Fetching the data from the database as a JSON array
    private JsonArrayRequest getDataFromServer(String email) {

        //JsonArrayRequest of volley
        return new JsonArrayRequest(webURL + String.valueOf(email),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Calling method to parse the json response
                        try {
                            parseData(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //If an error occurs that means end of the list has reached
                    }
                });
    }

    private void getData(String email){

        requestQueue.add(getDataFromServer(email));
    }

    private void renderItems(String email){

        getData(email);
    }

    //  Parsing data from database and adding it to an arraylist (for easy access)
    private void parseData(JSONArray array) throws JSONException {

        Log.d("Profile Items",String.valueOf(array.getJSONObject(0)));


        String user_ID="",fname ="",lname = "", date_birth="", balance="",email="";
        for (int i = 0; i < array.length(); i++) {

            //Creating the Request object
            JSONObject json = null;

            try {
                json = array.getJSONObject(i);

                //Adding data to the request object
                user_ID = json.getString("user_id");
                fname = json.getString("firstname");
                lname = json.getString("lastname");
                date_birth = json.getString("dateofbirth");
                email = json.getString("email");
                balance = json.getString("balance");


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        //Notifying the adapter that data has been added or changed
//        adapter.notifyDataSetChanged();

       String Full = fname + " " + lname;

        String B = "";
        if(balance == ""){
            B ="R0";
        }
        else
        {
            B = "R"+balance;
        }

        name.setText(Full);
        Balance.setText(B);
        Email.setText(email);
        date.setText(date_birth);
        userId.setText(user_ID);

    }

}