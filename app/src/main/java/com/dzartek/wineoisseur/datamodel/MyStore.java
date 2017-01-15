package com.dzartek.wineoisseur.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dzarrillo on 11/1/2016.
 */

public class MyStore  implements Parcelable{
    private String name;
    private double latitude;
    private double longitude;
    private String icon;
    private String opennow;
    private int pricelevel;
    private String rating;
    private String vicinity;
    private String htmlattributions;
    private String placeid;


    public MyStore(){}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String isOpennow() {
        return opennow;
    }

    public void setOpennow(String opennow) {
        this.opennow = opennow;
    }

    public int getPricelevel() {
        return pricelevel;
    }

    public void setPricelevel(int pricelevel) {
        this.pricelevel = pricelevel;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getHtmlattributions() {
        return htmlattributions;
    }

    public void setHtmlattributions(String htmlattributions) {
        this.htmlattributions = htmlattributions;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // The following has to be read in the same order in Parcelable.Creator
        parcel.writeString(name);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(icon);
        parcel.writeString(opennow);
        parcel.writeInt(pricelevel);
        parcel.writeString(rating);
        parcel.writeString(vicinity);
        parcel.writeString(htmlattributions);
        parcel.writeString(placeid);
    }

    public MyStore(Parcel input){
        // Read the same as it was written
        name=input.readString();
        latitude=input.readDouble();
        longitude=input.readDouble();
        icon=input.readString();
        opennow=input.readString();
        pricelevel=input.readInt();
        rating=input.readString();
        vicinity=input.readString();
        htmlattributions=input.readString();
        placeid=input.readString();
    }

    public static final Creator<MyStore> CREATOR = new Creator<MyStore>() {
        @Override
        public MyStore createFromParcel(Parcel parcel) {
            return new MyStore(parcel);
        }

        @Override
        public MyStore[] newArray(int i) {
            return new MyStore[i];
        }
    };
}
