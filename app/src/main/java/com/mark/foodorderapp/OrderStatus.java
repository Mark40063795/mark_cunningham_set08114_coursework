package com.mark.foodorderapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mark.foodorderapp.Common.Common;
import com.mark.foodorderapp.Model.Request;
import com.mark.foodorderapp.ViewHolder.OrderViewHolder;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("requests");

        recyclerView = (RecyclerView)findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());
    }

    private void loadOrders(String phone) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.orderID.setText(adapter.getRef(position).getKey());
                viewHolder.orderStatus.setText(codeToStatus(model.getStatus()));
                viewHolder.orderAddress.setText(model.getAddress());
                viewHolder.orderPhone.setText(model.getPhone());
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private String codeToStatus(String status) {
        //Basically redundant, due to time constraints haven't added the ability to update order status from a second app for employees

        if(status.equals("0"))
            return "Order Confirmed";
        else if(status.equals("1"))
            return "Out For Delivery";
        else
            return "Order error";
    }
}
