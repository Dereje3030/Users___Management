package com.ethioptech.usermanagement.model;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ethioptech.usermanagement.R;

public class UserView extends RecyclerView.ViewHolder {
    private View view;
    public UserView(@NonNull View itemView) {
        super(itemView);
        view=itemView;
    }
    public void setUsername(String username){
        TextView textView=view.findViewById(R.id.user_name);
        textView.setText(username);
    }
}
