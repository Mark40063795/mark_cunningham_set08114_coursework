package com.mark.foodorderapp;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mark.foodorderapp.Common.Common;
import com.mark.foodorderapp.Model.Order;
import com.mark.foodorderapp.Model.Request;
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
    DatabaseReference requests;

    TextView totalPrice;
    FButton btnPlace;

    CartAdapter adapter;

    List<Order> cart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance();
        requests=database.getReference("requests");

        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutmanager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutmanager);
        
        totalPrice = (TextView)findViewById(R.id.total);
        btnPlace = (FButton)findViewById(R.id.orderBtn);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cart.size()>0)
                    alertDialog();
                else
                    Toast.makeText(Cart.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            }
        });

        loadOrder();
    }

    private void alertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Now we need to know where you are!");
        alertDialog.setMessage("Please enter your address: ");

        final EditText edtAddress = new EditText (Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress);
        alertDialog.setIcon(R.drawable.cart);

        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edtAddress.getText().toString(),
                        totalPrice.getText().toString(),
                        cart
                );
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                new Database(getBaseContext()).emptyCart();
                Toast.makeText(Cart.this, "Order Confirmed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void loadOrder()
    {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        int total = 0;
        for (Order order:cart)
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        Locale locale =new Locale("en","GB");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        totalPrice.setText(fmt.format(total));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return true;
    }

    private void deleteCart(int position) {
        //removes item from cart, deletes data from sqlite db, update order in db, refresh order
        cart.remove(position);
        new Database(this).emptyCart();
        for(Order item:cart)
            new Database(this).addToCart(item);
        loadOrder();
    }
}
