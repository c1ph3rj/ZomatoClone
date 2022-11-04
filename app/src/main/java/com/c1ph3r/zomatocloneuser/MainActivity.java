package com.c1ph3r.zomatocloneuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
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

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding MAIN;
    private FirebaseAuth AUTH;
    String code;
    FragmentTransaction fragmentTransaction;
    Fragment fragment;
    String verificationId;
    private final static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Binding the layout to a variable.
        MAIN = ActivityMainBinding.inflate(getLayoutInflater());
        // Getting the root of the layout and passing that to the contentView.
        View view = MAIN.getRoot();
        setContentView(view);

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        getLocationPermission();

        // Firebase App Device Verification check.
        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        // Verify the mobile app by using play service instead opening a browser for captcha.
        firebaseAppCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance());
        // Firebase Initialization.
        AUTH = FirebaseAuth.getInstance();

        // On Click Continue Button it verifies the Mobile number with regex if the mobile number is verified it starts the OTP verification process
        MAIN.submit.setOnClickListener(sendOTP -> {
            if(Objects.requireNonNull(MAIN.mobileNumber.getText()).toString().matches(getString(R.string.MOBILE_NO_PATTERN))){
                // Method to send OTP.
                sendOTPToTheUser();
            }
            else{
                // If the mobile no does not match the regex, Toast a message to the user.
                Toast.makeText(this, getString(R.string.Not_Valid_Mobile_No), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getLocationPermission() {
        if(!(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    private void sendOTPToTheUser() {
        // Method to send OTP to the user.
        try{
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(AUTH)
                    .setPhoneNumber(getString(R.string.country_code_Text) + MAIN.mobileNumber.getText())
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(this)
                    .setCallbacks(sendOTPCallBacks).build();
            PhoneAuthProvider.verifyPhoneNumber(options);
            MAIN.loadingTimeToSendOTP.setVisibility(View.VISIBLE);
        }catch(Exception e){
            MAIN.loadingTimeToSendOTP.setVisibility(View.GONE);
            System.out.println(e.getMessage() + getString(R.string.exception_2));
        }
    }


    // After verification code tries to send this method will be called.
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks sendOTPCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        // After code is sent to the user.
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            verifyTheOTP(verificationId);
        }

        // After verification code successfully sent to the user. this method will be executes.
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            code = phoneAuthCredential.getSmsCode();

        }

        // If the attempt to send the code to the user is failed this method will be executed.
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            MAIN.loadingTimeToSendOTP.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, getString(R.string.exception_1), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }
    };

    private void verifyTheOTP(String verificationId) {
        MAIN.loadingTimeToSendOTP.setVisibility(View.GONE);
        System.out.println(MAIN.mobileNumber.getText());
        fragment = new OTP_Verification(Objects.requireNonNull(MAIN.mobileNumber.getText()).toString(), AUTH, verificationId);
        fragmentTransaction = getSupportFragmentManager().beginTransaction().add(R.id.Login_Activity, fragment).addToBackStack("OnBackPressed");
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getSupportFragmentManager().getBackStackEntryCount()>0)
            getSupportFragmentManager().popBackStackImmediate();
    }
}