package com.appadia.beacon.Models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jorge on 18/03/2016.
 */

//Class Model of Deal, with its getter and setter
public class DealModel {
    private int dealid;
    private String type,deal_title,start_day,end_day,start_time,end_time,description,coupons,event_where,active,category,shops,images,saved_status;

    public DealModel() {

    }

    public void setDealid(int dealid) {
        this.dealid = dealid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDeal_title(String deal_title) {
        this.deal_title = deal_title;
    }

    public void setEnd_day(String end_day) {
        this.end_day = end_day;
    }

    public void setStart_day(String start_day) {
        this.start_day = start_day;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCoupons(String coupons) {
        this.coupons = coupons;
    }

    public void setEvent_where(String event_where) {
        this.event_where = event_where;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setShops(String shops) {
        this.shops = shops;
    }

    public void setSaved_status(String saved_status) {
        this.saved_status = saved_status;
    }

    public DealModel(int dealid, String type, String deal_title, String start_day, String end_day, String start_time, String end_time, String description, String coupons, String event_where, String active, String category, String shops, String images, String saved_status) {
        this.dealid = dealid;
        this.type = type;
        this.deal_title = deal_title;
        this.start_day = start_day;
        this.end_day = end_day;
        this.start_time = start_time;
        this.end_time = end_time;
        this.description = description;
        this.coupons = coupons;
        this.event_where = event_where;
        this.active = active;
        this.category = category;
        this.shops = shops;
        this.images = images;
        this.saved_status = saved_status;
    }

    public int getDealid() {
        return dealid;
    }

    public String getType() {
        return type;
    }

    public String getDeal_title() {
        return deal_title;
    }

    public String getStart_day() {
        return start_day;
    }

    public String getEnd_day() {
        return end_day;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getDescription() {
        return description;
    }

    public String getCoupons() {
        return coupons;
    }

    public String getEvent_where() {
        return event_where;
    }

    public String getActive() {
        return active;
    }

    public String getCategory() {
        return category;
    }

    public String getShops() {
        return shops;
    }

    public String getImages() {
        return images;
    }

    public String getSaved_status() {
        return saved_status;
    }
}
