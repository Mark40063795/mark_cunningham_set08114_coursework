package com.mark.foodorderapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import com.rey.material.widget.CheckBox;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mark.foodorderapp.Common.Common;
import com.mark.foodorderapp.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {
    EditText phoneLogin, passLogin;
    Button loginBtn;
    com.rey.material.widget.CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneLogin = (MaterialEditText)findViewById(R.id.phoneLogin);
        passLogin = (MaterialEditText)findViewById(R.id.passLogin);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        checkBox = (CheckBox)findViewById(R.id.checkBox);

        //paper to save name & pass
        Paper.init(this);

        //Code for firebase integration
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("user");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //implement connection checker
                if (Common.checkConnection(getBaseContext())) {

                    //saving phone + pass at login
                    if(checkBox.isChecked())
                    {
                        Paper.book().write(Common.USER_KEY,phoneLogin.getText().toString());
                        Paper.book().write(Common.PWD_KEY,passLogin.getText().toString());

                    }

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
                                user.setPhone(phoneLogin.getText().toString());
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

                else
                {
                    Toast.makeText(Login.this, "Please check your connection to the internet", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }
}
