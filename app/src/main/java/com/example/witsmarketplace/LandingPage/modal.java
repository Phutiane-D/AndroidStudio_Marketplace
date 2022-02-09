package com.example.witsmarketplace.LandingPage;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.witsmarketplace.Login.ServerCommunicator;
import com.example.witsmarketplace.R;
import com.example.witsmarketplace.SharedPreference;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class modal extends AppCompatActivity
{
    private static String name;
    private static String stock;
    private static String price;
    private static String description;
    private static String productID;
    private static ArrayList<String> images = new ArrayList<>();
    private static ExtendedFloatingActionButton cartBtn;
    private static ExtendedFloatingActionButton faveBtn;
    public static SharedPreference sharedPreference;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modal);
        mContext = this;

        TextView name_text = findViewById(R.id.name);
        TextView price_text = findViewById(R.id.price);
        TextView desc_text = findViewById(R.id.desc);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        price = intent.getStringExtra("price");
        description = intent.getStringExtra("description");
        images = intent.getStringArrayListExtra("images_array");
        productID = intent.getStringExtra("productID");
        stock = intent.getStringExtra("stock");


        name_text.setText(name);
        price_text.setText(price);
        desc_text.setText(description);

        cartBtn = (ExtendedFloatingActionButton) findViewById(R.id.addCartBtn);
        faveBtn = (ExtendedFloatingActionButton) findViewById(R.id.addFaveBtn);
        sharedPreference = new SharedPreference(this);
        String email = sharedPreference.getSH("email");

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddToCart(email,name,description,images.get(0),price,productID,stock);
            }
        });
        faveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddToFav(email,name,description,images.get(0),price,productID);
                Log.d("ADD TO WISHLIST","PRODUCT ADDED TO WISHLIST HAS ID ==    "+productID);
            }
        });

        ImageSlider imageSlider = findViewById(R.id.modal_image_slider);
        List<SlideModel> slideModels = new ArrayList<>();
        for(String image:images){
            slideModels.add(new SlideModel(image));
        }
        imageSlider.setImageList(slideModels, true);
    }

    // add to cart button implementation
    public void AddToCart(String email, String name, String description, String picture, String price,String productID, String stock){
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL", email);
        contentValues.put("NAME", name);
        contentValues.put("PICTURE", picture);
        contentValues.put("DESCRIPTION", description);
        contentValues.put("PRICE", price);
        contentValues.put("PRODUCT_ID",productID);
        contentValues.put("STOCK", stock);
        new ServerCommunicator("https://lamp.ms.wits.ac.za/home/s2172765/app_add_cart.php", contentValues) {
            @Override
            protected void onPreExecute() {}

            @Override

            protected void onPostExecute(String output) {
                try {
                    JSONArray users = new JSONArray(output);
                    JSONObject object = users.getJSONObject(0);

                    String status = object.getString("add_status");
                    String message = object.getString("status_message");

                    if(status.equals("1")){
                        Toast.makeText(mContext ,"Added to cart",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(mContext, message , Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }

    // add to favorite button implementation
    public void AddToFav(String email, String name, String description, String picture, String price,String productID){
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL", email);
        contentValues.put("NAME", name);
        contentValues.put("PICTURE", picture);
        contentValues.put("DESCRIPTION", description);
        contentValues.put("PRICE", price);
        contentValues.put("PRODUCT_ID",productID);

        new ServerCommunicator("https://lamp.ms.wits.ac.za/home/s2172765/app_add_fav.php", contentValues) {
            @Override
            protected void onPreExecute() {}

            @Override

            protected void onPostExecute(String output) {
                try {
                    JSONArray users = new JSONArray(output);
                    JSONObject object = users.getJSONObject(0);

                    String status = object.getString("add_status");
                    String message = object.getString("status_message");

                    if(status.equals("1")){
                        Toast.makeText(mContext ,"Added to favorite",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(mContext, message , Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }

}