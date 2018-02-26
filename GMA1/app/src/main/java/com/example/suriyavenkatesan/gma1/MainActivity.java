package com.example.suriyavenkatesan.gma1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    //assign firebase authentication object
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //creating instance for firebase authentication
        auth=FirebaseAuth.getInstance();
        //Intializing widgets
        TextView treg=(TextView)findViewById(R.id.textreg);
        Button btlog=(Button)findViewById(R.id.btnlog);
        final ProgressDialog p2 =new ProgressDialog(this);
        final EditText email= (EditText) findViewById(R.id.editemail);
        final EditText epass=(EditText)findViewById(R.id.editpass);
        boolean connected = false;

        //creating object to Connectivity Manager
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //checking connection state
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        //Dispalying notification to switch on internet
       if (connected) {
        }
        else{
            AlertDialog alert=new AlertDialog.Builder(MainActivity.this).create();
            alert.setTitle("Notification");
            alert.setMessage("Please Turn on the Internet.");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alert.show();
        }
        //setting onclick listener for registration button
                        treg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, signup.class));
                        finish();
                    }
                });
       //setting onclick listener for login button
                btlog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        p2.setTitle("Logging in");
                        p2.setMessage("Please Wait...");
                        p2.setCanceledOnTouchOutside(false);
                        p2.show();
                        final String email2 = email.getText().toString().trim();
                        final String password = epass.getText().toString().trim();
                        if (TextUtils.isEmpty(email2)) {
                            p2.dismiss();
                            Toast.makeText(MainActivity.this, "Email is required...", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(password)) {
                            p2.dismiss();
                            Toast.makeText(MainActivity.this, "Password is required...", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //After checking the text fields signing in
                        auth.signInWithEmailAndPassword(email2, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    p2.dismiss();
                                    Toast.makeText(MainActivity.this, "Login successfull....", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this,welcome1.class));
                                    finish();
                                    return;
                                } else {
                                    p2.dismiss();
                                    Toast.makeText(MainActivity.this, "Login unsuccessfull...", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });

                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
