package com.ethioptech.usermanagement;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ethioptech.usermanagement.model.RecyclerItemClickListener;
import com.ethioptech.usermanagement.model.User;
import com.ethioptech.usermanagement.model.UserAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
public class UsersPageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<User> users=new ArrayList<>();
    private FirestoreRecyclerAdapter<User,UserView> adapter;
    String userId;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_page);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final FirebaseFirestore db=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        Query query=db.collection("users").orderBy("username", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<User> options=new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query,User.class).build();
        adapter=new FirestoreRecyclerAdapter<User, UserView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserView holder, final int position, @NonNull User model) {
                holder.textView.setText(model.getUsername());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(UsersPageActivity.this,UserDetailActivity.class);
                        userId=adapter.getSnapshots().getSnapshot(position).getId();
                        intent.putExtra("key",userId);
                        startActivity(intent);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final String userId=adapter.getSnapshots().getSnapshot(position).getId();
                        if(userId.equals(firebaseAuth.getCurrentUser().getUid())){
                            Toast.makeText(getApplicationContext(),"user mustb be logged out ",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            DocumentReference documentReference=db.collection("users").document(userId);
                            documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"user deleted succsessfully",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        return false;
                    }
                });
            }
            @NonNull
            @Override
            public UserView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list,parent,false);
                return new UserView(view);
            }
        };
        recyclerView.setAdapter(adapter);
    }
    private class UserView extends RecyclerView.ViewHolder{
        public TextView textView;
        public UserView(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.user_name);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null){
            adapter.stopListening();
        }
    }
    private void logout(){
        firebaseAuth.signOut();
        startActivity(new Intent(UsersPageActivity.this,MainActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        logout();
        return super.onOptionsItemSelected(item);
    }
}