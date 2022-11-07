package com.c1ph3r.zomatocloneuser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class OTP_Verification extends Fragment {
    String mobileNumber;
    FirebaseAuth AUTH;
    PhoneAuthCredential phoneAuthCredential;
    String verificationId;
    private FirebaseFirestore userDB;
    ProgressBar loading;
    Address address;
    DocumentReference user;


    public OTP_Verification() {
        // Required empty public constructor
    }

    // Initializing the required variables using constructor.
    public OTP_Verification(String mobileNumber, FirebaseAuth AUTH, String verificationId) {
        this.mobileNumber = mobileNumber;
        this.AUTH = AUTH;
        this.verificationId = verificationId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Getting the layout in the view.
        View view =  inflater.inflate(R.layout.fragment_o_t_p__verification, container, false);

        // If the view acquired this method will be executed.
        if(view!=null){
            // Initializing the required variables.
            MaterialButton verifyOTP = view.findViewById(R.id.verifyButton);
            EditText OTP = view.findViewById(R.id.OTPField);
            MaterialButton back = view.findViewById(R.id.backToLogin);
            loading = view.findViewById(R.id.loadingForUserData);
            TextView mobileNumberDisplay = view.findViewById(R.id.mobileNumberDisplay);
            this.mobileNumber = getString(R.string.country_code_Text)+ mobileNumber;
            mobileNumberDisplay.setText(mobileNumber);
            userDB = FirebaseFirestore.getInstance();


            // Verifying the OTP using firebase auth.
            verifyOTP.setOnClickListener(OnClickVerify ->{
                if(OTP.getText().length() == 6 ){
                    // Getting the credentials of the user to verify the user.
                    phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, OTP.getText().toString());
                    // Method to verify the user.
                    loading.setVisibility(View.VISIBLE);
                    signInTheUser();
                }else
                    // if the password is wrong alter the user.
                    Toast.makeText(requireActivity(), R.string.ValidOTP_Error_Text, Toast.LENGTH_SHORT).show();
            });

            // On Click of back button.
            back.setOnClickListener(OnClickBack -> requireActivity().getSupportFragmentManager().popBackStack());
        }

        // Returning the view.
        return view;
    }

    // Method to signInTheUser in firebase auth.
    private void signInTheUser() {
        AUTH.signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                // If successfully signed in change the activity.
                ifTheUserIsNew();
                // alter the user about the login status.
                //Toast.makeText(requireActivity(), R.string.LoginStatus1, Toast.LENGTH_SHORT).show();
            }
            else
                // alter the user if the login failed.
                Toast.makeText(requireActivity(), R.string.LoginStatus_2, Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(Throwable::printStackTrace);
    }

    private void ifTheUserIsNew() {
        if(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() != null){
            user = userDB.collection("userDataBase")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
            user.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot userDetails) {
                    if(userDetails.exists()){
                        loading.setVisibility(View.INVISIBLE);
                        changePage();
                    }else{
                        loading.setVisibility(View.INVISIBLE);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loading.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            });
        }

    }


    // Method to Change Activity based on the Location permission access.
    void changePage(){
        // If the user already allows the permission it forwards to the dashboard.
        // Else it force the user to allows the permission.
        Intent intent;

        if(!(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            intent = new Intent(requireActivity(), EnableLocationPermission.class);
        }else{
            saveUserLocation();
            intent = new Intent(requireActivity(), Dashboard.class);
        }
        startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    private void saveUserLocation() {
            CancellationTokenSource ct = new CancellationTokenSource();
            ct.getToken();
            FusedLocationProviderClient fusedLocation = LocationServices.getFusedLocationProviderClient(requireActivity());
            fusedLocation.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.getToken()).addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Geocoder fetchAddress = new Geocoder(requireActivity(), Locale.getDefault());
                    List<Address> addresses;
                    try {
                        addresses= fetchAddress.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        address  = addresses.get(0);
                        System.out.println(address.getAddressLine(0));
                        user.update("currentAddress", address.getAddressLine(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(Throwable::printStackTrace);

    }// End Of UserLocation
}