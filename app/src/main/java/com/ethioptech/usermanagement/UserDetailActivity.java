package com.ethioptech.usermanagement;
import android.content.Intent;
import android.os.Bundle;
import com.ethioptech.usermanagement.model.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
public class UserDetailActivity extends AppCompatActivity {
    private TextView fullname;
    private TextView username;
    private TextView sex;
    private TextView phone;
    private TextView email;
    private User user;
    String userId;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        fullname=findViewById(R.id.fullname);
        username=findViewById(R.id.username);
        sex=findViewById(R.id.sex);
        phone=findViewById(R.id.phone);
        email=findViewById(R.id.email);
        Intent intent=getIntent();
        userId=intent.getStringExtra("key");
        firestore=FirebaseFirestore.getInstance();
        DocumentReference documentReference=firestore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                fullname.setText(documentSnapshot.getString("firstname")+" "+documentSnapshot.getString("lastname"));
                username.setText(documentSnapshot.getString("username"));
                sex.setText(documentSnapshot.getString("sex"));
                phone.setText(documentSnapshot.getString("phonenumber"));
                email.setText(documentSnapshot.getString("email"));
            }
        });
    }
}
