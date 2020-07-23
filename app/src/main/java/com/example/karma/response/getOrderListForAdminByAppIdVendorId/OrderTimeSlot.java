package com.example.karma.response.getOrderListForAdminByAppIdVendorId;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderTimeSlot {
    @SerializedName("deliveryDate")
    @Expose
    private String deliveryDate;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
