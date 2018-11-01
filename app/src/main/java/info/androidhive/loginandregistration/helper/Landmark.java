package info.androidhive.loginandregistration.helper;

import android.media.Image;
import android.support.annotation.NonNull;

public class Landmark implements Comparable {

    private String name;
    private double distance;
    private double rate;
    private String image;
    private String PhoneNumber;
    private double latitude;
    private double longitude;


    public Landmark(String name,double distance,double rate,String image,String Phone){
        this.name=name;
        this.distance=distance;
        this.rate=rate;
        this.image=image;
        this.PhoneNumber=Phone;
    }


    public String getName() {
        return name;
    }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public String getPhoneNumber() { return PhoneNumber; }

    public void setPhoneNumber(String phoneNumber) { PhoneNumber = phoneNumber; }

    public void setName(String name) { this.name = name; }

    public double getDistance() {
        return distance;
    }

    public double getRate() {
        return rate;
    }

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

    @Override
    public int compareTo(@NonNull Object o) {
        return (int)(((Landmark)o).getRate()-this.getRate());
    }
}