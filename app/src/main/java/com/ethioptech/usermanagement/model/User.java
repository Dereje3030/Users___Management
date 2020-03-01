package com.ethioptech.usermanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String email;
    private String firstname;
    private String lastname;
    private String phonenumber;
    private String sex;
    private String username;

    public User() {

    }

    public User(String email, String firstname, String lastname, String password, String phonenumber, String sex, String username) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phonenumber = phonenumber;
        this.sex = sex;
        this.username = username;
    }

    protected User(Parcel in) {
        email = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        phonenumber = in.readString();
        sex = in.readString();
        username = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getSex() {
        return sex;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(phonenumber);
        dest.writeString(sex);
        dest.writeString(username);
    }
}