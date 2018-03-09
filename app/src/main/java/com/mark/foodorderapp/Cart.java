package com.mark.foodorderapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mark.foodorderapp.Model.Order;
import com.mark.foodorderapp.ViewHolder.CartAdapter;
import com.mark.foodorderapp.database.Database;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutmanager;

    FirebaseDatabase database;
    DatabaseReference request;

    TextView totalPrice;
    FButton btnPlace;

    CartAdapter adapter;

    List<Order> cart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance();
        request=database.getReference("requests");

        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutmanager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutmanager);
        
        totalPrice = (TextView)findViewById(R.id.total);
        btnPlace = (FButton)findViewById(R.id.orderBtn);
        
        loadOrder();
    }

    private void loadOrder()
    {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        int total = 0;
        for (Order order:cart)
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        Locale locale =new Locale("en","GB");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        totalPrice.setText(fmt.format(total));
    }
}
