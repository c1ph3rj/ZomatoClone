package com.c1ph3r.zomatocloneuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.c1ph3r.zomatocloneuser.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding MAIN;
    private FirebaseAuth AUTH;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MAIN = ActivityMainBinding.inflate(getLayoutInflater());
        View view = MAIN.getRoot();
        setContentView(view);


        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance());
        AUTH = FirebaseAuth.getInstance();


        MAIN.submit.setOnClickListener(sendOTP -> {
            if(MAIN.mobileNumber.getText().toString().matches("^[6-9][0-9]{9}$")){
                sendOTPToTheUser();
            }
            else{
                Toast.makeText(this, "Enter a valid Mobile Number!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendOTPToTheUser() {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(AUTH)
                .setPhoneNumber("+91" + MAIN.mobileNumber.getText())
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(sendOTPCallBacks).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }



    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks sendOTPCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

        }
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            code = phoneAuthCredential.getSmsCode();
            if(code != null);


        }
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(MainActivity.this, "Unable To Send Code!", Toast.LENGTH_SHORT).show();
            System.out.println(e);
        }
    };


}