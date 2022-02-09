package com.example.witsmarketplace.fave_cart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.witsmarketplace.Login.LoginActivity;
import com.example.witsmarketplace.OrderHistory.OrderHistory_Item;
import com.example.witsmarketplace.R;
import com.example.witsmarketplace.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class Address extends AppCompatActivity {
    String products="", name="", order_no="", address="", date="", total="", items="", street="", city="", surburb="", country="";
    public static final String Prefs = "sharedprefs ";
    public static SharedPreference sharedPreference;
    String order_historyURL = "https://lamp.ms.wits.ac.za/home/s2172765/app_fetch_address.php?USER_EMAIL=";
    private RequestQueue requestQueue;
    private TextView streets;
    EditText Street_Name ;
    EditText City_Name ;
    EditText Suburb_Name ;
    EditText Country_Name ;
    Button Continue ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
         Street_Name = findViewById(R.id.Street);
         City_Name = findViewById(R.id.City);
         Suburb_Name = findViewById(R.id.Suburb);
         Country_Name = findViewById(R.id.Country);
         Continue = findViewById(R.id.Continue);

        streets = findViewById(R.id.as_street);

        requestQueue = Volley.newRequestQueue(this);
        getData();

        sharedPreference = new SharedPreference(this);

        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Street = Street_Name.getText().toString().trim();
                String City = City_Name.getText().toString().trim();
                String Suburb = Suburb_Name.getText().toString().trim();
                String Country = Country_Name.getText().toString().trim();

                if(Street.isEmpty())
                    Street_Name.setError("This Field Is Required");
                else if(Suburb.isEmpty())
                    Suburb_Name.setError("This Field Is Required");
                else if(City.isEmpty())
                    City_Name.setError("This Field Is Required");
                else if(Country.isEmpty())
                    Country_Name.setError("This Field Is Required");
                else if(Street.isEmpty() && Suburb.isEmpty() && City.isEmpty() && Country.isEmpty()){
                    Toast.makeText(Address.this,"Incomplete fields",Toast.LENGTH_SHORT).show();
                }
                else{
                    String address = Street + "," + City + "," + Suburb +"," + Country;
                    sharedPreference.setSH("Address", address);

                    Intent intent = new Intent(Address.this, Summery.class);
                    startActivity(intent);
                }

            }
        });

    }

    private void parseData(JSONArray array) {


        String[] total_str = new String[50];
        int[] total_int = new int[total_str.length];
        int total_pmt = 0;

        String[] name_str = new String[50];
        String str = "";

        for (int i = 0; i < array.length(); i++) {

            //Creating the Request object
            JSONObject json = null;

            try {

                json = array.getJSONObject(i);

                address = json.getString("ADDRESS");



                JSONObject address_obj = new JSONObject(address);
                street = address_obj.getString("Street");
                surburb = address_obj.getString("Surburb");
                city = address_obj.getString("City");
                country = address_obj.getString("Country");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            OrderHistory_Item v = new OrderHistory_Item("R " + total_pmt, date, street, surburb, city, country, items, name_str, total_str, order_no);
//            order_history_items.add(v);
            System.out.println(address);
            streets.setText(street + ", " + surburb + ", " + city + ", " + country);
            streets.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Street_Name.setText(street);
                    Suburb_Name.setText(surburb);
                    City_Name.setText(city);
                    Country_Name.setText(country);
                }
            });
        }
    }

    //  Fetching the data from the database as a JSON array
    private JsonArrayRequest getDataFromServer(String url, String requestCount) {

        //JsonArrayRequest of volley
        return new JsonArrayRequest(url + requestCount,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Calling method to parse the json response

                        parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //If an error occurs that means end of the list has reached
                    }
                });
    }

    private void getData(){

        SharedPreference sharedPreference = new SharedPreference(this);
        String Email = sharedPreference.getSH("email");


        requestQueue.add(getDataFromServer(order_historyURL, Email));
    }
    public void errorhandling(){
        if (TextUtils.isEmpty(Street_Name.getText().toString().trim())) {
            Street_Name.setError("ENTER STREET NAME");
        }
        if (TextUtils.isEmpty(Suburb_Name.getText().toString().trim())) {
            Suburb_Name.setError("ENTER SUBURB NAME");
        }
        if (TextUtils.isEmpty(City_Name.getText().toString().trim())) {
            City_Name.setError("ENTER CITY NAME");
        }
        if (TextUtils.isEmpty(Country_Name.getText().toString().trim())) {
            Country_Name.setError("ENTER COUNTRY NAME");
        }
    }


}
