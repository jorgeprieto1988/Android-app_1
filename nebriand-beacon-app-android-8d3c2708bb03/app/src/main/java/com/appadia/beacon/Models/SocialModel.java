package com.appadia.beacon.Models;

/**
 * Created by Jorge on 06/04/2016.
 */

//Class Model of Social, with its getter and setter
public class SocialModel {
    private int socialid, shopid,mallid;
    private String sm_name,sm_link;

    public SocialModel() {

    }



    public SocialModel(int socialid, int shopid,int mallid, String sm_name, String sm_link) {
        this.socialid = socialid;
        this.shopid = shopid;
        this.sm_name = sm_name;
        this.sm_link = sm_link;

    }

    public int getShopid() {
        return shopid;
    }

    public void setShopid(int shopid) {
        this.shopid = shopid;
    }

    public int getMallid() {
        return mallid;
    }

    public void setMallid(int mallid) {
        this.mallid = mallid;
    }

    public int getSocialid() {
        return socialid;
    }

    public void setSocialid(int socialid) {
        this.socialid = socialid;
    }

    public String getSm_name() {
        return sm_name;
    }

    public void setSm_name(String sm_name) {
        this.sm_name = sm_name;
    }

    public String getSm_link() {
        return sm_link;
    }

    public void setSm_link(String sm_link) {
        this.sm_link = sm_link;
    }
}
