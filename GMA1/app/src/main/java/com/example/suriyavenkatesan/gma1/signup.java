package com.example.suriyavenkatesan.gma1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static android.widget.Toast.*;

public class signup extends AppCompatActivity {
    private FirebaseAuth auth;                                  //creating authentication object
    private DatabaseReference dbUser;                           //creating database reference object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Initializing widigets
        final ProgressDialog p1=new ProgressDialog(this);
        final EditText ename=(EditText)findViewById(R.id.editname);
        final EditText email=(EditText)findViewById(R.id.editemail);
        final EditText epass=(EditText)findViewById(R.id.editpass);
        Button breg=(Button)findViewById(R.id.btnreg);
        ImageButton im=(ImageButton)findViewById(R.id.image);

        auth=FirebaseAuth.getInstance();                        //Instantiating authentication object

        //Back button to return to login page
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signup.this,MainActivity.class));
                finish();
            }
        });
        //defining function of register button
        breg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p1.setTitle("Registering user");
                p1.setMessage("Please Wait...");
                p1.setCanceledOnTouchOutside(false);
                p1.show();
                final String email1 = email.getText().toString().trim();
                final String password = epass.getText().toString().trim();
                final String name=ename.getText().toString().trim();
                if (TextUtils.isEmpty(email1)) {
                    p1.dismiss();
                    Toast.makeText(getApplicationContext(), "Email is required...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    p1.dismiss();
                    Toast.makeText(getApplicationContext(), "Password is required...", Toast.LENGTH_SHORT).show();
                    return;
                }
                //creating user with email and password in firebase
                auth.createUserWithEmailAndPassword(email1, password).addOnCompleteListener((Activity) signup.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        p1.setMessage("Registering user...");
                        p1.show();
                        if (task.isSuccessful()) {
                            String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();    //fetching the user id
                            //fetching the data from real time firebase database using user id.
                            dbUser = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                            HashMap<String,String> userMap=new HashMap<>();
                            userMap.put("name",name);
                            userMap.put("email",email1);
                            userMap.put("password",password);
                            dbUser.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                               p1.dismiss();
                               startActivity(new Intent(getApplicationContext(),welcome.class));
                                   finish();
                                }
                            });
                        } else {
                            p1.dismiss();
                            Toast.makeText(getApplicationContext(), "Registration unsuccessfull...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
