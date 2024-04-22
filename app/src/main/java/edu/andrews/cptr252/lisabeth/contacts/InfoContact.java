package edu.andrews.cptr252.lisabeth.contacts;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class InfoContact implements Parcelable {
    private String name = "";

    private String phone = "";
    private String address = "";
    private String photo = "";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private  Long id = -1L;

    public InfoContact(){

    }
    private  InfoContact(Parcel source){
        String[] data = new String[5];
        source.readStringArray(data);
        setName(data[0]);
        setPhone(data[1]);
        setAddress(data[2]);
        setPhoto(data[3]);
        setId(Long.parseLong(data[4]));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                getName(),getPhone(),getAddress(),getPhoto(), String.valueOf(getId())
        });
    }

    public static final Parcelable.Creator<InfoContact> CREATOR = new Parcelable.Creator<InfoContact>(){

        @Override
        public InfoContact createFromParcel(Parcel source){
            return new InfoContact(source);
        }
        @Override
        public InfoContact[] newArray(int size){
            return new InfoContact[0];
        }
    };
}
