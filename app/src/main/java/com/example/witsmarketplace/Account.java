package com.example.witsmarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.witsmarketplace.LandingPage.LandingPage;
import com.example.witsmarketplace.Login.LoginActivity;
import com.example.witsmarketplace.OrderHistory.OrderHistory;
import com.example.witsmarketplace.ProfilePage.Profile;
import com.example.witsmarketplace.fave_cart.cart;
import com.example.witsmarketplace.fave_cart.favorite;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Account extends AppCompatActivity {
    TextView orders,profile;
    ImageButton back;
    BottomNavigationView bnv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        navigations();

    }
    // navigation buttons in one function
    public void navigations(){
        bnv = findViewById(R.id.bottom_navigation);
        orders = (TextView) findViewById(R.id.orders);
        profile = (TextView)findViewById(R.id.profile);
        back = (ImageButton) findViewById(R.id.backbtn);

        bnv.setOnNavigationItemSelectedListener(navListener);
        bnv.getMenu().getItem(3).setChecked(true);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrderHistory.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LandingPage.class);
                startActivity(intent);
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent intent = null;
                    if (item.getItemId() == R.id.nav_cart){
                        intent = new Intent(getApplicationContext(), cart.class);
                        startActivity(intent);
                    }
                    else if (item.getItemId() == R.id.nav_favorite) {
                        intent = new Intent(getApplicationContext(), favorite.class);
                        startActivity(intent);
                    }
                    else if(item.getItemId() == R.id.nav_home){
                        intent = new Intent(getApplicationContext(),LandingPage.class);
                        startActivity(intent);
                    }
                    return true;
                }
            };
}