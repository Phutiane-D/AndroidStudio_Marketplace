package com.example.witsmarketplace.OrderHistory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.witsmarketplace.LandingPage.ItemBox;
import com.example.witsmarketplace.LandingPage.Itembox_Adapter;
import com.example.witsmarketplace.R;
import com.example.witsmarketplace.SharedPreference;
import com.example.witsmarketplace.fave_cart.CartItem;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>  {
    private Context mContext;
    private Activity context;
    private String[] prices;
    private String[] names;

    public static class OrdersViewHolder extends RecyclerView.ViewHolder {

        public TextView order_name;
        public TextView order_price;

        //      view holder for directly setting the items' details to be displayed

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            order_name = itemView.findViewById(R.id.orders_name);
            order_price = itemView.findViewById(R.id.orders_price);
        }
    }

    public OrdersAdapter(String[] names, String[] prices){
        this.names = names;
        this.prices = prices;
    }

    @NonNull
    @Override
    public OrdersAdapter.OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_item, parent, false);

        return new OrdersAdapter.OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.OrdersViewHolder holder, int position) {
//      Set the view holders with details from the items list
        String current_name = names[position];
        String current_price = prices[position];

        holder.order_name.setText(current_name);
        holder.order_price.setText(String.valueOf(current_price));

    }

    @Override
    public int getItemCount() {
        return prices.length;
    }

}