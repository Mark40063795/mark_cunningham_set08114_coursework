package com.mark.foodorderapp;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mark.foodorderapp.Common.Common;
import com.mark.foodorderapp.Model.Food;
import com.mark.foodorderapp.Model.Order;
import com.mark.foodorderapp.database.Database;
import com.squareup.picasso.Picasso;

public class FoodDetails extends AppCompatActivity {

    TextView food_name, food_price, food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
    String foodId="";
    FirebaseDatabase database;
    DatabaseReference foods;
    Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                ));

                Toast.makeText(FoodDetails.this, "Added to cart", Toast.LENGTH_SHORT).show();

            }
        });

        food_description = (TextView)findViewById(R.id.food_description);
        food_name = (TextView)findViewById(R.id.food_name);
        food_price = (TextView)findViewById(R.id.food_price);
        food_image = (ImageView)findViewById(R.id.food_image);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        if(getIntent() !=null)
            foodId = getIntent().getStringExtra("FoodId");
        if(!foodId.isEmpty())
        {
            if(Common.checkConnection(getBaseContext()))
                 getFoodDetails(foodId);
            else
            {
                Toast.makeText(FoodDetails.this, "Please check your connection to the internet", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void getFoodDetails(String foodId)
    {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);

                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(food_image);

                collapsingToolbarLayout.setTitle(currentFood.getName());
                food_price.setText(currentFood.getPrice());
                food_name.setText(currentFood.getName());
                food_description.setText(currentFood.getDescription());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
