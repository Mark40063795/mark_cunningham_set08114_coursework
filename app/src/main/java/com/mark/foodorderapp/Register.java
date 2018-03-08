package com.mark.foodorderapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mark.foodorderapp.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Register extends AppCompatActivity {

    MaterialEditText phoneRegister, nameRegister, passRegister;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameRegister = (MaterialEditText)findViewById(R.id.nameRegister);
        passRegister = (MaterialEditText)findViewById(R.id.passRegister);
        phoneRegister = (MaterialEditText)findViewById(R.id.phoneRegister);

        registerBtn = (Button)findViewById(R.id.registerBtn);

        //Code for firebase integration
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("user");

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(Register.this);
                mDialog.setMessage("Please Wait :)");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Check if the phone number already exists
                        if(dataSnapshot.child(phoneRegister.getText().toString()).exists())
                        {
                            mDialog.dismiss();
                            Toast.makeText(Register.this, "This phone number is already in use", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            mDialog.dismiss();
                            User user = new User(nameRegister.getText().toString(),passRegister.getText().toString());
                            table_user.child(phoneRegister.getText().toString()).setValue(user);
                            Toast.makeText(Register.this, "Account Created", Toast.LENGTH_SHORT).show();
                            finish();
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
