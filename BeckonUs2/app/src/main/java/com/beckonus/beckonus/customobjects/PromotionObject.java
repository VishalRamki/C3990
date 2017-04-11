package com.beckonus.beckonus.customobjects;

/**
 * Created by Shal on 09/04/2017.
 */

public class PromotionObject {
    private String active;
    private String coupon;
    private String expires;
    private String id;
    private String message;
    private String present;
    private String promotionImage;
    private String promotion_id;
    private String store_id;
    private String title;
    private String storeName;
    public String Lat = "";
    public String Long = "";
    public String inClientDateTime;

    public void setLonLat(String Lon, String Lat) {
        this.Lat = Lat;
        this.Long = Lon;
    }

    public String getLatitude() {
        return this.Lat;
    }
    public String getLongtitude() {
        return this.Long;
    }

    public String getStore_id() {
        return this.store_id;
    }

    public void setStoreName(String storeName) {
      this.storeName = storeName;
    };

    public String getStoreName() {
        return this.storeName;
    }

    public String getPromotion_id() {
        return this.promotion_id;
    }

    public String getExpires() {
        return this.expires;
    }

    public String getMessage() {
        return this.message;
    }

    public String getTitle() {
        return this.title;
    }

    public String getImageLocation() {
      return "http://45.63.106.236:5000/api/materials/" + this.getFileName(this.promotionImage);
    };

    private String getFileName(String s) {
      return s.substring(this.promotionImage.lastIndexOf('/')+1, s.length());
    };
}
