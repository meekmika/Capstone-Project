package com.meekmika.warsart.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class StreetArt implements Parcelable {

    private String id;
    private String title;
    private String originalTitle;
    private String artist;
    private String description;
    private String address;
    private List<String> images;
    private double lat;
    private double lng;

    public StreetArt() {}

    protected StreetArt(Parcel in) {
        id = in.readString();
        title = in.readString();
        originalTitle = in.readString();
        artist = in.readString();
        description = in.readString();
        address = in.readString();
        images = in.createStringArrayList();
    }

    public static final Creator<StreetArt> CREATOR = new Creator<StreetArt>() {
        @Override
        public StreetArt createFromParcel(Parcel in) {
            return new StreetArt(in);
        }

        @Override
        public StreetArt[] newArray(int size) {
            return new StreetArt[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(artist);
        dest.writeString(description);
        dest.writeString(address);
        dest.writeStringList(images);
    }

    @Override
    public String toString() {
        return id + ", " + title;
    }
}
