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

public class OrderItem_Adapter extends RecyclerView.Adapter<OrderItem_Adapter.OrderItem_ViewHolder>  {
    private Context mContext;
    private  int resource;
    private Activity context;
    private ArrayList<OrderHistory_Item> itemsList = new ArrayList<OrderHistory_Item>();


    public static class OrderItem_ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemsDate;
        public TextView itemsAddress;
        public TextView itemsTotal;
        public TextView NoItems;
        public LinearLayout order_item;

        //      view holder for directly setting the items' details to be displayed

        public OrderItem_ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemsDate = itemView.findViewById(R.id.oh_date);
            itemsAddress = itemView.findViewById(R.id.address_id);
            itemsTotal = itemView.findViewById(R.id.total_id);
            NoItems = itemView.findViewById(R.id.NoItems_id);
            order_item = itemView.findViewById(R.id.orderTab);
        }
    }

    public OrderItem_Adapter(ArrayList<OrderHistory_Item> itemsList, Activity mContext, int n){
        this.mContext = mContext;
        this.itemsList = itemsList;
        this.context = mContext;
    }

    @NonNull
    @Override
    public OrderItem_Adapter.OrderItem_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item, parent, false);

        return new OrderItem_Adapter.OrderItem_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItem_Adapter.OrderItem_ViewHolder holder, int position) {
//      Set the view holders with details from the items list
        OrderHistory_Item currentItem = itemsList.get(position);

        holder.itemsDate.setText(currentItem.getDate());
        holder.itemsTotal.setText(currentItem.getTotal());

        holder.itemsAddress.setText(currentItem.getStreet() + ", "
                                    + currentItem.getSurburb()+ ", "
                                    + currentItem.getCity()+ ", "
                                    + currentItem.getCountry());

        holder.NoItems.setText(currentItem.getItems());

        holder.order_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreference sharedPreference = new SharedPreference(context);
                String Email = sharedPreference.getSH("email");

                Intent intent = new Intent(mContext, Orders.class);
                intent.putExtra("date", currentItem.getDate());
                intent.putExtra("street", currentItem.getStreet());
                intent.putExtra("surburb", currentItem.getSurburb());
                intent.putExtra("city", currentItem.getCity());
                intent.putExtra("country", currentItem.getCountry());
                intent.putExtra("names", currentItem.getName());
                intent.putExtra("prices", currentItem.getPrice());
                intent.putExtra("order_no", currentItem.getOrder_no());
                intent.putExtra("total", currentItem.getTotal());
                intent.putExtra("user", Email);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

}
