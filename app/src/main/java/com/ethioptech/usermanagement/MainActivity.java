package com.ethioptech.usermanagement;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private Button register;
    private EditText email;
    private EditText password;
    private FirebaseAuth firebaseAuth;
    private Button show;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.loginbtn);
        progressBar=findViewById(R.id.progressBar);
        show=findViewById(R.id.showHideBtn);
        register=findViewById(R.id.registerbtn);
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null){
            Intent intent=new Intent(MainActivity.this,UsersPageActivity.class);
            startActivity(intent);
            finish();
        }
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(show.getText().toString().equals("Show")){
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    show.setText("Hide");
                }
                else{
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    show.setText("Show");
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em=email.getText().toString().trim();
                String pass=password.getText().toString().trim();
                if (TextUtils.isEmpty(em)){
                    email.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    password.setError("Password is required");
                    return;
                }
                if(pass.length()<6){
                    password.setError("Password must be > 6 characters");
                    return;
                }
                boolean connected;
                ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()==NetworkInfo.State.CONNECTED){
                    connected=true;
                }
                else{
                    connected=false;
                }
                if(connected){
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(em,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent=new Intent(MainActivity.this,UsersPageActivity.class);
                                startActivity(intent);
                            }
                            else{
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(),"Your Email or Password is incorrect",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"please connect to internet",Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }
}