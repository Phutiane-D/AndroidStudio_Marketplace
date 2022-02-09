package com.example.witsmarketplace.LandingPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.witsmarketplace.R;
import com.example.witsmarketplace.fave_cart.cart;
import com.example.witsmarketplace.fave_cart.favorite;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("ALL")
public class SearchResults extends AppCompatActivity {

    private RequestQueue requestQueue;
    ArrayList<ItemBox> search_results = new ArrayList<ItemBox>();
    String searchURL = "https://lamp.ms.wits.ac.za/home/s2172765/Search.php?ID=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        requestQueue = Volley.newRequestQueue(this);


        TextView search_key = findViewById(R.id.search_key);

//      Set search key
        Bundle b = getIntent().getExtras();
        String search = b.getString("search");
        search_key.setText('"' + search + '"');

//      set search result items
//        Bundle args = getIntent().getBundleExtra("search_bundle");
//        ArrayList<ItemBox> list = (ArrayList<ItemBox>) args.getSerializable("search_result");

        getData(search);

        ImageView search_btn = findViewById(R.id.btn_search);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearch();
            }
        });

        //        Bottom Navigation
        BottomNavigationView bnv = findViewById(R.id.bottom_navigation);
        bnv.setOnNavigationItemSelectedListener(navListener);
    }

    // Bottom Navigation
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
                    else if(item.getItemId() == R.id.nav_cart){
                        intent = new Intent(getApplicationContext(), cart.class);
                        startActivity(intent);
                    }

                    return true;
                }
            };

    void openSearch(){
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
        finish();
    }

    private void parseData(JSONArray array, String count) {
        int productID=0;
        String name="", price="", image="", description="", stock ="";
        for (int i = 0; i < array.length(); i++) {

            //Creating the Request object
            JSONObject json = null;

            try {
                json = array.getJSONObject(i);

                //Adding data to the request object
                name = json.getString("NAME");
                price = json.getString("PRICE");
                image = json.getString("PICTURE");
                description = json.getString("DESCRIPTION");
                productID = json.getInt("PRODUCT_ID");
                stock = json.getString("ON_HAND");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the request object to the list
            String[] imageURLs = image.split(",");
            ArrayList<String> images = new ArrayList<>();
            images.addAll(Arrays.asList(imageURLs));
            String image_url = imageURLs[0];

            search_results.add(new ItemBox(productID,name, "R " + price, image_url, description,images,stock));
        }
        renderer(search_results);
    }

    private JsonArrayRequest getDataFromServer(String url, String requestCount) {

        //JsonArrayRequest of volley
        return new JsonArrayRequest(url + requestCount,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Calling method to parse the json response
                        parseData(response, requestCount);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //If an error occurs that means end of the list has reached
                    }
                });
    }

    private void getData(String key){
        requestQueue.add(getDataFromServer(searchURL, key));

    }

    private void renderEverything(){
//        getData(search);
    }

    private void renderer(ArrayList<ItemBox> list){
        //display the data in a recyclerview which allows us to scroll through
        RecyclerView recyclerView = findViewById(R.id.rv_searchResults);
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new Itembox_Adapter(list, this, 2);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }
}