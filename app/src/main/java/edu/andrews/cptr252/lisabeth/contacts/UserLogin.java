package edu.andrews.cptr252.lisabeth.contacts;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserLogin implements Parcelable {
    private String userId = "";
    private String username = "";
    private String password = "";



    private UserLogin(Parcel source){
        String[] data = new String[3];
        source.readStringArray(data);
        setUserId(data[0]);
        setUsername(data[1]);
        setPassword(data[2]);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                getUserId(),getUsername(),getPassword()
        });
    }

    public static final Creator<UserLogin> CREATOR = new Creator<UserLogin>(){

        @Override
        public UserLogin createFromParcel(Parcel source){
            return new UserLogin(source);
        }
        @Override
        public UserLogin[] newArray(int size){
            return new UserLogin[size];
        }
    };
}
