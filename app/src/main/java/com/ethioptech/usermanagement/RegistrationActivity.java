package com.ethioptech.usermanagement;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    private EditText firstname;
    private EditText lastname;
    private EditText username;
    private EditText password;
    private EditText email;
    private EditText phone;
    private String sex;
    private RadioGroup radioGroup;
    private Button register;
    private FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private String userId;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        firstname=findViewById(R.id.first_name);
        lastname=findViewById(R.id.last_name);
        username=findViewById(R.id.user_name);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email_address);
        progressBar=findViewById(R.id.progressBar);
        phone=findViewById(R.id.phone_number);
        radioGroup=findViewById(R.id.radio_group);
        register=findViewById(R.id.registerbtn);
        firebaseAuth=FirebaseAuth.getInstance();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton=findViewById(checkedId);
                sex=radioButton.getText().toString();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        final String em=email.getText().toString().trim();
                        final String pass=password.getText().toString().trim();
                        final String fname=firstname.getText().toString();
                        final String lname=lastname.getText().toString();
                        final String phonenumber=phone.getText().toString();
                        final String uname=username.getText().toString();
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
                    firebaseAuth.createUserWithEmailAndPassword(em,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(getApplicationContext(),"You are successfull man",Toast.LENGTH_SHORT);
                            userId=firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=firebaseFirestore.collection("users").document(userId);
                            Map<String,Object> user=new HashMap<>();
                            user.put("email",em);
                            user.put("firstname",fname);
                            user.put("lastname",lname);
                            user.put("phonenumber",phonenumber);
                            user.put("sex",sex);
                            user.put("username",uname);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"Are you good ?",Toast.LENGTH_SHORT);
                                    Intent intent=new Intent(RegistrationActivity.this,UsersPageActivity.class);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

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