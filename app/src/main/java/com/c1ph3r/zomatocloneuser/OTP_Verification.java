package com.c1ph3r.zomatocloneuser;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;


public class OTP_Verification extends Fragment {
    String mobileNumber, code;
    FirebaseAuth AUTH;
    PhoneAuthCredential phoneAuthCredential;

    public OTP_Verification() {
        // Required empty public constructor
    }

    public OTP_Verification(String mobileNumber, String code, FirebaseAuth AUTH, PhoneAuthCredential phoneAuthCredential) {
        this.mobileNumber = mobileNumber;
        this.code = code;
        this.AUTH = AUTH;
        this.phoneAuthCredential = phoneAuthCredential;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_o_t_p__verification, container, false);


        if(view!=null){
            MaterialButton verifyOTP = view.findViewById(R.id.verifyButton);
            EditText OTP = view.findViewById(R.id.OTPField);
            MaterialButton back = view.findViewById(R.id.backToLogin);

            verifyOTP.setOnClickListener(OnClickVerify ->{
                if(!OTP.getText().toString().equals(code))
                    Toast.makeText(requireActivity(), "Wrong OTP!", Toast.LENGTH_SHORT).show();
                else {
                    startActivity(new Intent(requireActivity(), Dashboard.class));
                    signInTheUser();
                    Toast.makeText(requireActivity(), "Successfully Logged In.", Toast.LENGTH_SHORT).show();
                }
            });


            back.setOnClickListener(OnClickBack -> requireActivity().getSupportFragmentManager().popBackStack());
        }


        return view;
    }

    private void signInTheUser() {
        AUTH.signInWithCustomToken(mobileNumber).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseFirestore Database = FirebaseFirestore.getInstance();
                    Toast.makeText(requireActivity(), "Logged In.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(requireActivity(), "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        TextView mobileNumberDisplay = view.findViewById(R.id.mobileNumberDisplay);
        this.mobileNumber = getString(R.string.country_code_Text)+ mobileNumber;
        System.out.println(mobileNumber);
        mobileNumberDisplay.setText(mobileNumber);

    }
}