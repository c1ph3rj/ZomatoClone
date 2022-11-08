package com.c1ph3r.zomatocloneuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.c1ph3r.zomatocloneuser.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Register extends AppCompatActivity {
    FirebaseFirestore userDB;
    DocumentReference user;
    private ActivityRegisterBinding REGISTER;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        REGISTER = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = REGISTER.getRoot();
        setContentView(view);

        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
        userDB = FirebaseFirestore.getInstance();
        user = userDB.collection("userDataBase").document(userID);

       REGISTER.nextBtn.setOnClickListener(onClickNext-> {
           REGISTER.loadingForUserName.setVisibility(View.VISIBLE);
           updateUserName(Objects.requireNonNull(REGISTER.userName.getText()).toString());
       });

    }

    public void updateUserName( String userName){
        user.update("userName", userName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                REGISTER.loadingForUserName.setVisibility(View.INVISIBLE);
                startActivity(new Intent(Register.this, Dashboard.class));
                finish();
            }
        }).addOnFailureListener(e -> {
            REGISTER.loadingForUserName.setVisibility(View.INVISIBLE);
            Toast.makeText(Register.this, "Failed to add! Check your Internet.", Toast.LENGTH_SHORT).show();
        });
    }

}