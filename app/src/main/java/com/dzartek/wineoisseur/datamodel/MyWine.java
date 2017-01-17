package com.dzartek.wineoisseur.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dzarrillo on 10/13/2016.
 */

public class MyWine implements Parcelable{
    private int id;
    private String region;
    private String varietal;
    private String name;
    private String code;
    private String winery;
    private String price;
    private String vintage;
    private String link;
    private String image;
    private long snoothrank;

    public MyWine(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getVarietal() {
        return varietal;
    }

    public void setVarietal(String varietal) {
        this.varietal = varietal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWinery() {
        return winery;
    }

    public void setWinery(String winery) {
        this.winery = winery;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVintage() {
        return vintage;
    }

    public void setVintage(String vintage) {
        this.vintage = vintage;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getSnoothrank() {
        return snoothrank;
    }

    public void setSnoothrank(long snoothrank) {
        this.snoothrank = snoothrank;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // The following has to be read in the same order in Parcelable.Creator
        parcel.writeInt(id);
        parcel.writeString(region);
        parcel.writeString(varietal);
        parcel.writeString(name);
        parcel.writeString(code);
        parcel.writeString(winery);
        parcel.writeString(price);
        parcel.writeString(vintage);
        parcel.writeString(link);
        parcel.writeString(image);
        parcel.writeLong(snoothrank);
    }

    public MyWine(Parcel input){
        // Read the same as it was written
        id=input.readInt();
        region=input.readString();
        varietal=input.readString();
        name=input.readString();
        code=input.readString();
        winery=input.readString();
        price=input.readString();
        vintage=input.readString();
        link=input.readString();
        image=input.readString();
        snoothrank=input.readLong();
    }

    public static final Creator<MyWine> CREATOR
            = new Creator<MyWine>() {
        public MyWine createFromParcel(Parcel in) {
            return new MyWine(in);
        }

        public MyWine[] newArray(int size) {
            return new MyWine[size];
        }
    };
}
