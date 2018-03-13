package com.mark.foodorderapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mark.foodorderapp.Common.Common;
import com.mark.foodorderapp.Model.User;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button loginBtn, registerBtn;
    TextView slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);

        slogan = (TextView)findViewById(R.id.slogan);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Allura.otf");
        slogan.setTypeface(face);

        //Initialising paper for auto login
        Paper.init(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(MainActivity.this,Login.class);
                startActivity(login);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(MainActivity.this,Register.class);
                startActivity(register);
            }
        });
        
        //Check to see if phone + pass is remember, if it is then login automatically
        String user = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);
        if(user != null && pwd != null)
        {
            if(!user.isEmpty() && !pwd.isEmpty())
                login(user,pwd);
        }

    }

    private void login(final String phone, final String pwd) {
        //Copy+paste of code from login page to take care of auto login. has some slight changes

        //Code for firebase integration
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("user");

        if (Common.checkConnection(getBaseContext())) {

            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Please Wait :)");
            mDialog.show();

            ValueEventListener valueEventListener = table_user.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //Check if user exists
                    if (dataSnapshot.child(phone).exists()) {

                        //Gets users information
                        mDialog.dismiss();
                        User user = dataSnapshot.child(phone).getValue(User.class);
                        user.setPhone(phone.toString());
                        if (user.getPassword().equals(pwd)) {
                            {
                                Toast.makeText(MainActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                                Intent homeIntent = new Intent(MainActivity.this, Home.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "That number has not been recognised. Please try again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        else
        {
            Toast.makeText(MainActivity.this, "Please check your connection to the internet", Toast.LENGTH_SHORT).show();
            return;
        }

    }
}
