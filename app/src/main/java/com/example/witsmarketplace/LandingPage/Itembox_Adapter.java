package com.example.witsmarketplace.LandingPage;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.witsmarketplace.Login.RegistrationActivity;
import com.example.witsmarketplace.Login.ServerCommunicator;
import com.example.witsmarketplace.R;
import com.example.witsmarketplace.SharedPreference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Itembox_Adapter extends RecyclerView.Adapter<Itembox_Adapter.Itembox_ViewHolder> implements Filterable {

    private ArrayList<ItemBox> itemsList;
    private ArrayList<ItemBox> CompleteItemsList;
    public static int n;

    private Activity mContext;
    public static SharedPreference sharedPreference;

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<ItemBox> filteredItems = new ArrayList<>();

            if (charSequence.toString().isEmpty())
                filteredItems.addAll(CompleteItemsList);
            else{
                for (int i = 0; i < CompleteItemsList.size(); i++){
                    if (CompleteItemsList.get(i).getName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredItems.add(CompleteItemsList.get(i));
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredItems;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            itemsList.clear();
            itemsList.addAll((Collection<? extends ItemBox>) filterResults.values);
        }
    };

    public static class Itembox_ViewHolder extends RecyclerView.ViewHolder{

        public ImageView itemImage;
        public TextView itemName;
        public TextView itemPrice;
        public TextView itemDesc, date, address;
        public Button AddCart;
        public ImageButton CartButton;
        public RelativeLayout relativeLayout;


//      view holder for directly setting the items' details to be displayed
        public Itembox_ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (Itembox_Adapter.n == 1){
                itemImage = itemView.findViewById(R.id.img_item);
                itemName = itemView.findViewById(R.id.txt_itemname);
                itemPrice = itemView.findViewById(R.id.txt_price);
                CartButton = itemView.findViewById(R.id.AddToCart);
                relativeLayout = itemView.findViewById(R.id.item_box);
            }
            else if (Itembox_Adapter.n == 2){
                itemImage = itemView.findViewById(R.id.vm_img_item);
                itemName = itemView.findViewById(R.id.vm_itemname);
                itemPrice = itemView.findViewById(R.id.vm_price);
                CartButton = itemView.findViewById(R.id.AddToCart);
                relativeLayout = itemView.findViewById(R.id.item_box);
            }
            else if (Itembox_Adapter.n == 3){
                itemImage = itemView.findViewById(R.id.fav_itemImg);
                itemName = itemView.findViewById(R.id.fv_itemname);
                itemPrice = itemView.findViewById(R.id.fv_price);
                itemDesc = itemView.findViewById(R.id.fav_itemDesc);
                AddCart = itemView.findViewById(R.id.fv_cart);
            }

            else if (Itembox_Adapter.n == 4){
                itemImage = itemView.findViewById(R.id.img_item);
                itemName = itemView.findViewById(R.id.cart_name);
                itemPrice = itemView.findViewById(R.id.cart_item_price);
                itemDesc = itemView.findViewById(R.id.cart_item_price);
//                AddCart = itemView.findViewById(R.id.incre_decr_btn);
            }

        }
    }

//  list of all items
    public Itembox_Adapter(ArrayList<ItemBox> itemsList, Activity mContext, int n){
        this.mContext = mContext;
        this.itemsList = itemsList;
        this.CompleteItemsList = new ArrayList<>(itemsList);
        Itembox_Adapter.n = n;
        sharedPreference = new SharedPreference(mContext);
    }

    // add to cart button implementation
    public void AddToCart(String email, String name, String description, String picture, String price,int productID, String stock){
        Log.d("Int id for ths 1 is : ", String.valueOf(productID));
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL", email);
        contentValues.put("NAME", name);
        contentValues.put("PICTURE", picture);
        contentValues.put("DESCRIPTION", description);
        contentValues.put("PRICE", price);
        contentValues.put("PRODUCT_ID",productID);
        contentValues.put("STOCK", stock);
        System.out.println(stock);

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
    @NonNull
    @Override
    public Itembox_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (Itembox_Adapter.n == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itembox, parent, false);
        }
        else if (Itembox_Adapter.n == 2) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_more, parent, false);
        }
        else if (Itembox_Adapter.n == 3) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fave_item, parent, false);
        }
        else if (Itembox_Adapter.n == 4) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        }
        return new Itembox_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Itembox_ViewHolder holder, int position) {
//      Set the view holders with details from the items list
        ItemBox currentItem = itemsList.get(position);

        Glide.with(mContext).load(currentItem.getImage()).into(holder.itemImage);

        holder.itemName.setText(currentItem.getName());
        holder.itemPrice.setText("R  "+currentItem.getPrice());


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, modal.class);
                intent.putExtra("name",currentItem.getName());
                intent.putExtra("price",currentItem.getPrice());
                intent.putExtra("description",currentItem.getDescription());
                intent.putExtra("productID",currentItem.getProductID());
                intent.putStringArrayListExtra("images_array", currentItem.getImageUrls());
                intent.putExtra("stock", currentItem.getStock());
                mContext.startActivity(intent);
            }
        });

        holder.CartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = sharedPreference.getSH("email");
                AddToCart(email, currentItem.getName(), currentItem.getDescription(), currentItem.getImage(), currentItem.getPrice(), currentItem.getProductID(), currentItem.getStock());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

}