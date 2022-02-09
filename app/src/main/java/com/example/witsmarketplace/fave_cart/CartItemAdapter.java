package com.example.witsmarketplace.fave_cart;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.example.witsmarketplace.LandingPage.LandingPage;
import com.example.witsmarketplace.Login.ServerCommunicator;
import com.example.witsmarketplace.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartItemAdapter extends ArrayAdapter<CartItem> {

    private Context mContext;
    private int resource;
    private  int itemCount = 1;
    private String productID;
    public static String InStock;

    String removeUrl = "https://lamp.ms.wits.ac.za/home/s2172765/app_rm_item_cart.php"; // remove cart item


    public CartItemAdapter(Context context, int resource, ArrayList<CartItem> cartItems) {
        super(context, resource, cartItems);
        this.mContext = context;
        this.resource = resource;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //cart item info
        String email = getItem(position).getEmail();
        String name = getItem(position).getName();
        String price = getItem(position).getPrice();
        String image = getItem(position).getImage();
        productID = getItem(position).getProductID();
        InStock = getItem(position).getStock();
        System.out.println(InStock + "," + name);

//        String count = getItem(position).getCount();
//        String quantity = getItem(position).getQuantity();


        //create cart object with info
        CartItem cartItem = new CartItem(email, name, price, image, productID, InStock);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(resource, parent, false);

        ImageView itemImage = (ImageView) convertView.findViewById(R.id.cart_image);
        TextView itemName = (TextView) convertView.findViewById(R.id.cart_name);
        TextView itemPrice = (TextView) convertView.findViewById(R.id.cart_item_price);
        TextView stockCount = (TextView) convertView.findViewById(R.id.inStock);

        Button increment = (Button) convertView.findViewById(R.id.increase);
        Button decrement = (Button) convertView.findViewById(R.id.decrease);
        TextView itemQuantity = (TextView) convertView.findViewById(R.id.count);

        Button cartRemove = (Button) convertView.findViewById(R.id.btn_remove);

        itemName.setText(name);
        itemPrice.setText(price);
        itemQuantity.setText(String.valueOf(itemCount));
        stockCount.setText(InStock);
        Glide.with(mContext).load(image).into(itemImage);

        cartRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tell the server to remove this cart item entry
                removeItem(cartItem.getEmail(), cartItem.getProductID());
            }
        });


        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemCount < Integer.parseInt(InStock)){
                    itemCount = itemCount + 1;
                    // update item count
                    itemQuantity.setText(String.valueOf(itemCount));
                    String[] R_price = price.split(" ");
                    int Price = Integer.parseInt(R_price[1]);
                    String prices = "R " + String.valueOf(Price * itemCount);
                    // update price of x-items
                    itemPrice.setText(prices);
                }
            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemCount > 1){
                    itemCount = itemCount - 1;
                    // update item count
                    itemQuantity.setText(String.valueOf(itemCount));
                    String[] R_price = price.split(" ");
                    int Price = Integer.parseInt(R_price[1]);
                    String prices = "R " + String.valueOf(Price * itemCount);
                    // update price of x-items
                    itemPrice.setText(prices);

                }
            }
        });

        return convertView;
    }

    public void removeItem(String email, String productID) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL", email);
        contentValues.put("PRODUCT_ID", productID);
        new ServerCommunicator(removeUrl, contentValues) {
            @Override
            protected void onPreExecute() {
            }

            @Override

            protected void onPostExecute(String output) {

                if (output.equals("1")) {

                    Toast.makeText(mContext, "Item removed from cart", Toast.LENGTH_LONG).show();
                    mContext.startActivity(new Intent(mContext,cart.class));
                } else {
                    Toast.makeText(mContext, output, Toast.LENGTH_LONG).show();
                }

            }
        }.execute();

    }
}
