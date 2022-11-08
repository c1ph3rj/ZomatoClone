package com.c1ph3r.zomatocloneuser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.c1ph3r.zomatocloneuser.databinding.ActivityRegisterBinding;
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
        // Binding the view with Layout.
        REGISTER = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = REGISTER.getRoot();
        setContentView(view);

        try {
            // Declaring variables.
            userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
            userDB = FirebaseFirestore.getInstance();
            user = userDB.collection(getString(R.string.userDataBase_Text)).document(userID);

            // On click of register button update the userValues.
            REGISTER.nextBtn.setOnClickListener(onClickNext -> {
                REGISTER.loadingForUserName.setVisibility(View.VISIBLE);
                updateUserName(Objects.requireNonNull(REGISTER.userName.getText()).toString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Method to update userData to the FireBaseFireStore.
    public void updateUserName(String userName) {
        try {
            // updating the user data to the db.
            user.update(getString(R.string.userName_Text), userName).addOnSuccessListener(unused -> {
                REGISTER.loadingForUserName.setVisibility(View.INVISIBLE);
                // If successful go to Dashboard.
                startActivity(new Intent(Register.this, Dashboard.class));
                finish();
            }).addOnFailureListener(e -> {
                // if failed shows the error to the user.
                REGISTER.loadingForUserName.setVisibility(View.INVISIBLE);
                Toast.makeText(Register.this, R.string.Exception_3, Toast.LENGTH_SHORT).show();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}