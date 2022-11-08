package com.c1ph3r.zomatocloneuser.FireBaseFireStore;

import com.c1ph3r.zomatocloneuser.Model.UserDetails;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DataBase {
    private CollectionReference userDB;
    private UserDetails userDetails;
    private DocumentReference user;

    public DataBase(){
        FirebaseFirestore userDataBase = FirebaseFirestore.getInstance();
        userDB = userDataBase.collection("userDataBase");
    }


    public UserDetails getUserData(String mobileNumber){
        user = userDB.document(mobileNumber);
        user.get().addOnSuccessListener(documentSnapshot -> userDetails = documentSnapshot.toObject(UserDetails.class));
        return userDetails;
    }



    public boolean addUserData(String mobileNumber ){
        List<String> listOfAddress, listOfOrders;
        listOfAddress = new ArrayList<>();
        listOfOrders = new ArrayList<>();
        UserDetails newUser = new UserDetails();
        newUser.setMobileNumber(mobileNumber);
        newUser.setAddress(new GeoPoint(0,0));
        newUser.setUserName(" ");
        newUser.setListOfAddress(listOfAddress);
        newUser.setOrderHistory(listOfOrders);
        newUser.setProfilePic(" ");
        return userDB.document(mobileNumber).set(newUser).isSuccessful();
    }

    public boolean updateUserLocation(String mobileNumber, GeoPoint geoPoint){
        return userDB.document(mobileNumber).update("address", geoPoint).isSuccessful();
    }

    public boolean updateUserName(String mobileNumber, String userName){
        return userDB.document(mobileNumber).update("userName", userName).isSuccessful();
    }

    public boolean updateListOfAddress(String mobileNumber, List<String> listOfAddress){
        return userDB.document(mobileNumber).update("listOfAddress", listOfAddress).isSuccessful();
    }

    public boolean updateListOfOrders(String mobileNumber, List<String> listOfOrders){
        return userDB.document(mobileNumber).update("listOfOrders", listOfOrders).isSuccessful();
    }

    public boolean updateProfilePic(String mobileNumber, String profilePic){
        return userDB.document().update("profilePic", profilePic).isSuccessful();
    }
}
