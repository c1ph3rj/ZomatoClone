package com.c1ph3r.zomatocloneuser.Model;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class UserDetails {
    private String userName;
    private List<String> listOfAddress;
    private GeoPoint address;
    private String mobileNumber;
    private List<String> orderHistory;
    private String profilePic;


    public UserDetails(String userName, List<String> listOfAddress, GeoPoint address, String mobileNumber, List<String> orderHistory, String profilePic) {
        this.userName = userName;
        this.listOfAddress = listOfAddress;
        this.address = address;
        this.mobileNumber = mobileNumber;
        this.orderHistory = orderHistory;
        this.profilePic = profilePic;
    }

    public UserDetails() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getListOfAddress() {
        return listOfAddress;
    }

    public void setListOfAddress(List<String> listOfAddress) {
        this.listOfAddress = listOfAddress;
    }

    public GeoPoint getAddress() {
        return address;
    }

    public void setAddress(GeoPoint address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public List<String> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<String> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

}
