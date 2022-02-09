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

import com.bumptech.glide.Glide;
import com.example.witsmarketplace.Login.ServerCommunicator;
import com.example.witsmarketplace.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavItemAdapter extends ArrayAdapter<FavItem> {

    private Context mContext;
    private  int resource;

    String addUrl = "https://lamp.ms.wits.ac.za/home/s2172765/app_add_cart.php"; // add item to cart
    String removeUrl = "https://lamp.ms.wits.ac.za/home/s2172765/app_rm_item_fav.php"; // remove item from wishlist

    public FavItemAdapter(Context context, int resource, ArrayList<FavItem> favItems){
        super(context,resource,favItems);
        this.mContext = context;
        this.resource = resource;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //fav item info
        String name =  getItem(position).getName();
        String price = getItem(position).getPrice();
        String image = getItem(position).getImage();
        String count = getItem(position).getCount();
        String desc = getItem(position).getDesc();
        String email = getItem(position).getEmail();
        String productID = getItem(position).getProductID();

        //create fav object with info
        FavItem favItem = new FavItem(name,price,image,count,desc,email,productID);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(resource,parent,false);

        ImageView itemImage = (ImageView) convertView.findViewById(R.id.fav_itemImg);
        TextView itemName =  (TextView) convertView.findViewById(R.id.fv_itemname);
        TextView itemPrice = (TextView) convertView.findViewById(R.id.fv_price);
        TextView itemDesc =  (TextView) convertView.findViewById(R.id.fav_itemDesc);
        TextView instock = (TextView) convertView.findViewById(R.id.instock);
        Button addcart = (Button) convertView.findViewById(R.id.fv_cart);
        Button remove = (Button) convertView.findViewById(R.id.fv_rm);

        itemName.setText(name);
        itemPrice.setText(price);
        itemDesc.setText(desc);
        //if(Integer.parseInt(count)>0)instock.setText("In stock");
        Glide.with(mContext).load(image).into(itemImage);

        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Fav Item clicked","Fav item clicked: Email ="+favItem.getEmail()+"    ProductID = "+favItem.getProductID());

                addToCart(email, name,desc, image, price, productID);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Fav Item clicked","Fav item clicked: Email ="+favItem.getEmail()+"    ProductID = "+favItem.getProductID());
                removeFromFav(email,productID);
            }
        });

        return convertView;
    }

    // add to cart button implementation
    public void addToCart(String email, String name, String description, String picture, String price,String productID){
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL", email);
        contentValues.put("NAME", name);
        contentValues.put("PICTURE", picture);
        contentValues.put("DESCRIPTION", description);
        contentValues.put("PRICE", price);
        contentValues.put("PRODUCT_ID",productID);

        new ServerCommunicator(addUrl, contentValues) {
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

    // add to cart button implementation
    public void removeFromFav(String email,String productID){
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL", email);
        contentValues.put("PRODUCT_ID",productID);

        new ServerCommunicator(removeUrl, contentValues) {
            @Override
            protected void onPreExecute() {}

            @Override

            protected void onPostExecute(String output) {
                if(output.equals("1")){

                    Toast.makeText(mContext ,"Removed from  favourites",Toast.LENGTH_LONG).show();
                    mContext.startActivity(new Intent(mContext,favorite.class));
                }
                else{
                    Toast.makeText(mContext, output , Toast.LENGTH_LONG).show();
                }

            }
        }.execute();

    }

}
