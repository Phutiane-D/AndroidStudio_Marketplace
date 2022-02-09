package com.example.witsmarketplace.fave_cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import com.example.witsmarketplace.Account;
import com.example.witsmarketplace.LandingPage.LandingPage;
import com.example.witsmarketplace.Login.ServerCommunicator;
import com.example.witsmarketplace.R;
import com.example.witsmarketplace.SharedPreference;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class cart extends AppCompatActivity {
    String webURL = "https://lamp.ms.wits.ac.za/home/s2172765/cart_items.php?ID="; // id == email
    String discardUrl = "https://lamp.ms.wits.ac.za/home/s2172765/discard_cart.php?ID="; // discard cart items


    private RequestQueue requestQueue;
    ImageButton backbtn;
    TextView cart_count;
    TextView totalPrice;

    ListView cartList;

    ArrayList<CartItem> cartItems = new ArrayList<CartItem>();
    SharedPreference sharedPreference;

    Button proceedToCheckout, discard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        sharedPreference = new SharedPreference(this);
        String userEmail = sharedPreference.getSH("email");

        requestQueue = Volley.newRequestQueue(this);
        renderItems(userEmail);

        //        Bottom Navigation
        BottomNavigationView bnv = findViewById(R.id.bottom_navigation);
        bnv.setOnNavigationItemSelectedListener(navListener);
        bnv.getMenu().getItem(1).setChecked(true);

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cart.this,LandingPage.class);
                startActivity(intent);
            }
        });

        cartList  = findViewById(R.id.cartList);
        proceedToCheckout =  findViewById(R.id.pcheckout);
        discard = findViewById(R.id.discard);
        proceedToCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cartItems.isEmpty()){
                    Intent intent = new Intent(cart.this, Address.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(cart.this,"Nothing has been added to cart",Toast.LENGTH_LONG).show();
                }
            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discardCart(userEmail);
            }
        });
    }

    //    Bottom Navigation
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;
                if (item.getItemId() == R.id.nav_home){
                    intent = new Intent(getApplicationContext(), LandingPage.class);
                    startActivity(intent);
                }
                else if (item.getItemId() == R.id.nav_favorite) {
                    intent = new Intent(getApplicationContext(), favorite.class);
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

        Log.d("Cart Items",String.valueOf(array.getJSONObject(0)));

        int tprice =0;
        String email="",name="", price="", image="", productID="", stock ="";
        for (int i = 0; i < array.length(); i++) {

            //Creating the Request object
            JSONObject json = null;

            try {
                json = array.getJSONObject(i);

                //Adding data to the request object
                email = json.getString("EMAIL");
                name = json.getString("NAME");
                price = json.getString("PRICE");
                image = json.getString("PICTURE");
                productID = json.getString("PRODUCT_ID");
                stock = json.getString("STOCK");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the request object to the list
            String[] imageURLs = image.split(",");
            String image_url = imageURLs[0];
            price = "R " + price;
            System.out.println(price);

            cartItems.add(new CartItem(email,name, price, image_url,productID,stock));
            price = price.substring(2);///////////////added this
            tprice += Integer.parseInt(price);
        }

        renderer();
        cart_count = findViewById(R.id.cart_count);
        cart_count.setText(cartItems.size()+" items");

        totalPrice = findViewById(R.id.cart_total);
        totalPrice.setText("R  "+tprice);
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
        ListView cartList  = findViewById(R.id.cartList);

        CartItemAdapter ad = new CartItemAdapter(cart.this, R.layout.cart_item, cartItems);
        cartList.setAdapter(ad);

    }

    public void discardCart(String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", email);

        new ServerCommunicator(discardUrl, contentValues) {
            @Override
            protected void onPreExecute() {}

            @Override

            protected void onPostExecute(String output) {
                if(output.equals("1")){

                    Toast.makeText(getApplicationContext() ,"Cart Discarded",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(cart.this, LandingPage.class));
                }
                else{
                    Toast.makeText(cart.this, output , Toast.LENGTH_LONG).show();
                }

            }
        }.execute();

    }

}