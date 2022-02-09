package com.example.witsmarketplace.OrderHistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.witsmarketplace.Account;
import com.example.witsmarketplace.OrderHistory.OrderHistory_Item;
import com.example.witsmarketplace.OrderHistory.OrderItem_Adapter;
import com.example.witsmarketplace.R;
import com.example.witsmarketplace.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Objects;
import java.util.zip.Inflater;

public class OrderHistory extends AppCompatActivity implements RecyclerView.OnScrollChangeListener {

    private RequestQueue requestQueue;
    ArrayList<OrderHistory_Item> order_history_items = new ArrayList<OrderHistory_Item>();
    String order_historyURL = "https://lamp.ms.wits.ac.za/home/s2172765/app_fetch_orderHistory.php?USER_EMAIL=";
    String order_NoURL = "https://lamp.ms.wits.ac.za/home/s2172765/app_fetch_orderNo.php?USER_EMAIL=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        requestQueue = Volley.newRequestQueue(this);
        getData();
        ImageButton backButton = findViewById(R.id.backbtn);
        // back button on order history
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Account.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void parseData2(JSONArray array) {
        String products="", name="", price="", image="", description="";
        for (int i = 0; i < array.length(); i++) {

            //Creating the Request object
            JSONObject json = null;

            try {
                // get looged in email
                SharedPreference sharedPreference = new SharedPreference(this);
                String Email = sharedPreference.getSH("email");
                
                json = array.getJSONObject(i);
                String orderNo = json.getString("ORDER_NO");
                System.out.println(orderNo);
                requestQueue.add(getDataFromServer2(order_historyURL, Email, orderNo));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void parseData(JSONArray array) {
        String products="", name="", order_no="", address="", date="", total="", items="", street="", city="", surburb="", country="";

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

                products = json.getString("PRODUCT_NAME");

                if (products.charAt(0) == '"'){
                    products = products.substring(1, products.length()-1);
                }

                address = json.getString("ADDRESS");
                order_no = json.getString("ORDER_NO");

                JSONObject address_obj = new JSONObject(address);
                street = address_obj.getString("Street");
                surburb = address_obj.getString("Surburb");
                city = address_obj.getString("City");
                country = address_obj.getString("Country");

                date = json.getString("DATE");

//              FROM APP
                if (products.charAt(0) == '{'){
                    JSONObject arr = new JSONObject(products);
                    name = arr.getString("NAME");
                    total = arr.getString("PRICE");

                    total_str = total.split(",");
                    total_int = new int[total_str.length];
                    total_pmt = 0;

                    items = String.valueOf(total_str.length);
                    for(int k = 0; k < total_str.length; k++) {
                        total_int[k] = Integer.parseInt(total_str[k]);
                        total_pmt += total_int[k];
                    }

//                  split product names
                    name = name.substring(0, name.length()-1);
                    name_str = name.split(",");
                }// FROM WEBSITE
                else{
                    JSONArray arr = new JSONArray(products);

                    for (int j = 0; j < arr.length();j++){
                    JSONObject obj = arr.getJSONObject(j);

                    total = obj.getString("PRICE");
                    name = obj.getString("NAME");
                    }

                    total = total.substring(1);
                    total_str = total.split(",");
                    total_int = new int[total_str.length];
                    total_pmt = 0;

                    items = String.valueOf(total_str.length);
                    for(int k = 0; k < total_str.length; k++) {
                        total_int[k] = Integer.parseInt(total_str[k]);
                        total_pmt += total_int[k];
                    }

//                  split product names
                    name = name.substring(1);
                    name_str = name.split(",");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            OrderHistory_Item v = new OrderHistory_Item("R " + total_pmt, date, street, surburb, city, country, items, name_str, total_str, order_no);
            order_history_items.add(v);
        }
        //Notifying the adapter that data has been added or changed
        //adapter.notifyDataSetChanged();
        renderer(order_history_items);
    }

    private JsonArrayRequest getDataFromServer(String url, String email) {

        //JsonArrayRequest of volley
        return new JsonArrayRequest(url + email,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Calling method to parse the json response
                        parseData2(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //If an error occurs that means end of the list has reached
                    }
                });
    }

    private JsonArrayRequest getDataFromServer2(String url, String email, String orderNo) {

        //JsonArrayRequest of volley
        return new JsonArrayRequest(url + email + "&ORDER_NO=" + orderNo,
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

        requestQueue.add(getDataFromServer(order_NoURL, Email));
    }

    private void renderer(ArrayList<OrderHistory_Item> list){
        //display the data in a recyclerview which allows us to scroll through
        RecyclerView recyclerView = findViewById(R.id.rv_order_history);
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new OrderItem_Adapter(list, this, 5);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

//    private  void
private boolean isLastItemDisplaying(RecyclerView recyclerView) {
    if (Objects.requireNonNull(recyclerView.getAdapter()).getItemCount() != 0) {
        int lastVisibleItemPosition = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findLastCompletelyVisibleItemPosition();

        return lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1;
    }
    return false;
}

@Override
public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
    //if Scrolled to the end then fetch more data
//    if (isLastItemDisplaying(RecyclerView.Adapter)) {
        //Calling the method getData again
//        renderCategories();
//    }
    }
}