package com.mark.foodorderapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mark.foodorderapp.Interface.ItemClickListener;
import com.mark.foodorderapp.R;

/**
 * Created by Mark on 12/03/2018.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView orderID, orderStatus, orderPhone, orderAddress;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);

        orderAddress = (TextView)itemView.findViewById(R.id.order_address);
        orderPhone = (TextView)itemView.findViewById(R.id.order_phone);
        orderStatus = (TextView)itemView.findViewById(R.id.order_status);
        orderID = (TextView)itemView.findViewById(R.id.order_number);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
