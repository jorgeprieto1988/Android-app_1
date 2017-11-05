package com.appadia.beacon.Models;

/**
 * Created by Jorge on 31/03/2016.
 */

//Class Model of Mall, with its getter and setter
public class MallModel {
    private int mallid,amenities_count;
    private String mall_name,type,phone_no,description,website,parking,cinema,email,opening_days,address,images,amenitiesshort,amenitieslong;

    public MallModel() {

    }

    public MallModel(int mallid, int amenities_count, String mall_name, String type, String phone_no, String description, String website, String parking, String cinema, String email, String opening_days, String address, String images, String amenitiesshort, String amenitieslong) {
        this.mallid = mallid;
        this.amenities_count = amenities_count;
        this.mall_name = mall_name;
        this.type = type;
        this.phone_no = phone_no;
        this.description = description;
        this.website = website;
        this.parking = parking;
        this.cinema = cinema;
        this.email = email;
        this.opening_days = opening_days;
        this.address = address;
        this.images = images;
        this.amenitiesshort = amenitiesshort;
        this.amenitieslong = amenitieslong;
    }

    public int getMallid() {
        return mallid;
    }

    public void setMallid(int mallid) {
        this.mallid = mallid;
    }

    public int getAmenities_count() {
        return amenities_count;
    }

    public void setAmenities_count(int amenities_count) {
        this.amenities_count = amenities_count;
    }

    public String getMall_name() {
        return mall_name;
    }

    public void setMall_name(String mall_name) {
        this.mall_name = mall_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCinema() {
        return cinema;
    }

    public void setCinema(String cinema) {
        this.cinema = cinema;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpening_days() {
        return opening_days;
    }

    public void setOpening_days(String opening_days) {
        this.opening_days = opening_days;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getAmenitiesshort() {
        return amenitiesshort;
    }

    public void setAmenitiesshort(String amenitiesshort) {
        this.amenitiesshort = amenitiesshort;
    }

    public String getAmenitieslong() {
        return amenitieslong;
    }

    public void setAmenitieslong(String amenitieslong) {
        this.amenitieslong = amenitieslong;
    }

}
