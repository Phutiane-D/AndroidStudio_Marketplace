package com.example.witsmarketplace.fave_cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.witsmarketplace.Account;
import com.example.witsmarketplace.LandingPage.LandingPage;
import com.example.witsmarketplace.LandingPage.modal;
import com.example.witsmarketplace.R;
import com.example.witsmarketplace.SharedPreference;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class favorite extends AppCompatActivity {

    String webURL = "https://lamp.ms.wits.ac.za/home/s2172765/favourites.php?ID="; // id == email

    private RequestQueue requestQueue;
    ImageButton backbtn;
    public static SharedPreference sharedPreference;

    ArrayList<FavItem> favItems = new ArrayList<FavItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        sharedPreference = new SharedPreference(this);
        String userEmail = sharedPreference.getSH("email");

        requestQueue = Volley.newRequestQueue(this);
        renderItems(userEmail);

        //        Bottom Navigation
        BottomNavigationView bnv = findViewById(R.id.bottom_navigation);
        bnv.setOnNavigationItemSelectedListener(navListener);
        bnv.getMenu().getItem(2).setChecked(true);

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(favorite.this,LandingPage.class);
                startActivity(intent);
            }
        });
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
                    else if (item.getItemId() == R.id.nav_home) {
                        intent = new Intent(getApplicationContext(), LandingPage.class);
                        startActivity(intent);
                    }
                    else if(item.getItemId() == R.id.nav_account) {
                        intent = new Intent(getApplicationContext(), Account.class);
                        startActivity(intent);
                    }

                    return true;
                }
            };

    //  Parsing data from database and adding it to an arraylist (for easy access)
    private void parseData(JSONArray array) throws JSONException {

        Log.d("WishList Items",String.valueOf(array.getJSONObject(0)));


        String name="", price="", image="", iCount="", desc="",email="",productID="";
        for (int i = 0; i < array.length(); i++) {

            //Creating the Request object
            JSONObject json = null;

            try {
                json = array.getJSONObject(i);

                //Adding data to the request object
                name = json.getString("NAME");
                price = json.getString("PRICE");
                image = json.getString("PICTURE");
                desc = json.getString("DESCRIPTION");
                email = json.getString("EMAIL");
                productID = json.getString("PRODUCT_ID");
                //iCount = json.getString("COUNT");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the request object to the list
            String[] imageURLs = image.split(",");
            String image_url = imageURLs[0];

            favItems.add(new FavItem(name, price, image_url, iCount,desc,email,productID));
        }
        //Notifying the adapter that data has been added or changed
//        adapter.notifyDataSetChanged();

        renderer();
    }

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

    private void renderer(){
        //ListView adapter for the wishlist items
        ListView wishList  = findViewById(R.id.wishlistview);

        FavItemAdapter ad = new FavItemAdapter(favorite.this, R.layout.fave_item, favItems);
        wishList.setAdapter(ad);
    }

}