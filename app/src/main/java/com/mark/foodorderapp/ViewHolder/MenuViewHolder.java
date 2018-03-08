package com.mark.foodorderapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mark.foodorderapp.Interface.ItemClickListener;
import com.mark.foodorderapp.R;

/**
 * Created by Mark on 24/02/2018.
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView menuName;
    public ImageView menuImage;

    private ItemClickListener itemClickListener;

    public MenuViewHolder(View itemView) {
        super(itemView);

        menuName = (TextView)itemView.findViewById(R.id.menu_name);
        menuImage = (ImageView)itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick (View v){
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
