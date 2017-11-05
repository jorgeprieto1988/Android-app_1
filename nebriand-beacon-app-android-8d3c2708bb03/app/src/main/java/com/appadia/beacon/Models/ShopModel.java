package com.appadia.beacon.Models;

/**
 * Created by Jorge on 31/03/2016.
 */

//Class Model of Shop, with its getter and setter
public class ShopModel {
    private int shopid;
    private String shop_name,phone_no,description,website,floor,type,email,category,social,open_hours,images,floorplan,beacons,saved_status,notify_status;

    public ShopModel() {

    }

    public void setShopid(int shopid) {
        this.shopid = shopid;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    public void setOpen_hours(String open_hours) {
        this.open_hours = open_hours;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setBeacons(String beacons) {
        this.beacons = beacons;
    }

    public void setFloorplan(String floorplan) {
        this.floorplan = floorplan;
    }

    public void setSaved_status(String saved_status) {
        this.saved_status = saved_status;
    }

    public void setNotify_status(String notify_status) {
        this.notify_status = notify_status;
    }

    public ShopModel(int shopid, String shop_name, String phone_no, String description, String website, String floor, String type, String email, String category, String social, String open_hours, String images, String floorplan, String beacons, String saved_status, String notify_status) {
        this.shopid = shopid;
        this.shop_name = shop_name;
        this.phone_no = phone_no;
        this.description = description;
        this.website = website;
        this.floor = floor;
        this.type = type;
        this.email = email;
        this.category = category;
        this.social = social;
        this.open_hours = open_hours;
        this.images = images;
        this.floorplan = floorplan;
        this.beacons = beacons;
        this.saved_status = saved_status;
        this.notify_status = notify_status;
    }

    public int getShopid() {
        return shopid;
    }

    public String getShop_name() {
        return shop_name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getDescription() {
        return description;
    }

    public String getWebsite() {
        return website;
    }

    public String getFloor() {
        return floor;
    }

    public String getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public String getCategory() {
        return category;
    }

    public String getSocial() {
        return social;
    }

    public String getOpen_hours() {
        return open_hours;
    }

    public String getImages() {
        return images;
    }

    public String getFloorplan() {
        return floorplan;
    }

    public String getBeacons() {
        return beacons;
    }

    public String getSaved_status() {
        return saved_status;
    }

    public String getNotify_status() {
        return notify_status;
    }
}
