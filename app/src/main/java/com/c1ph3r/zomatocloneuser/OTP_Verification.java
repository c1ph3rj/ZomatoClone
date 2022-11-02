package com.c1ph3r.zomatocloneuser;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.Objects;


public class OTP_Verification extends Fragment {
    String mobileNumber, code;


    public OTP_Verification() {
        // Required empty public constructor
    }

    public OTP_Verification(String mobileNumber, String code) {
        this.mobileNumber = mobileNumber;
        this.code = code;
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
            System.out.println(code);

            verifyOTP.setOnClickListener(OnClickVerify ->{
                if(!OTP.getText().toString().equals(code))
                    Toast.makeText(requireActivity(), "Wrong OTP!", Toast.LENGTH_SHORT).show();
                else
                    startActivity(new Intent(requireActivity(), Dashboard.class));
            });

            back.setOnClickListener(OnClickBack ->{
                requireActivity().getSupportFragmentManager().popBackStack();
            });
        }


        return view;
    }
}