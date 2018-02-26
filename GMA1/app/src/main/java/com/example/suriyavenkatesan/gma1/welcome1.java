package com.example.suriyavenkatesan.gma1;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class welcome1 extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener authlistener;            //Declare authentication listener
    private FirebaseAuth auth;                                      //Declare authentication object
    private DatabaseReference demoref;                              //Declare database reference
    String retName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome1);
        //Initializing widget and database referneces
        final TextView t1=(TextView)findViewById(R.id.textView);
        final TextView t2=(TextView)findViewById(R.id.textView2);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        demoref=FirebaseDatabase.getInstance().getReference("users");
        final String userid=FirebaseAuth.getInstance().getCurrentUser().getUid();


        //Intializing database refernce and fetching data
        demoref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                retName=dataSnapshot.child(userid).child("name").getValue().toString();         //Assign data name to retname
                if(user.isEmailVerified())
                    t2.setText("Hello "+retName);
                else
                    t2.setText("Hello");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//Setting textview value by checking emailverified
        if(user.isEmailVerified()){
            t1.setText("Email verified.");
        }
        else{
            t1.setText("Email is not verified, click here.");
            t1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Sending Email verification
                    user.sendEmailVerification().addOnCompleteListener(welcome1.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(welcome1.this,"Email verification sent...",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(welcome1.this,"Email verification not sent...",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        }

        auth=FirebaseAuth.getInstance();            //Initializing firebase authentication instance
        Button btsignout=(Button)findViewById(R.id.btnsignout);
        //Describing operation for signout
        btsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(welcome1.this,MainActivity.class));
                finish();
            }
        });
    }
}
