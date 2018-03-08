package com.mark.foodorderapp;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mark.foodorderapp.Common.Common;
import com.mark.foodorderapp.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Login extends AppCompatActivity {
    EditText phoneLogin, passLogin;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneLogin = (MaterialEditText)findViewById(R.id.phoneLogin);
        passLogin = (MaterialEditText)findViewById(R.id.passLogin);
        loginBtn = (Button)findViewById(R.id.loginBtn);

        //Code for firebase integration
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("user");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(Login.this);
                mDialog.setMessage("Please Wait :)");
                mDialog.show();

                ValueEventListener valueEventListener = table_user.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Check if user exists
                        if (dataSnapshot.child(phoneLogin.getText().toString()).exists()) {
                            //Gets users information
                            mDialog.dismiss();
                            User user = dataSnapshot.child(phoneLogin.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(passLogin.getText().toString())) {
                                {
                                    Toast.makeText(Login.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                                    Intent homeIntent = new Intent(Login.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(Login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(Login.this, "That number has not been recognised. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
