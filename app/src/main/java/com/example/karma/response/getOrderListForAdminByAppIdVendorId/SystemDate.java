
package com.example.karma.response.getOrderListForAdminByAppIdVendorId;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SystemDate {

    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("modifiedt")
    @Expose
    private Object modifiedt;
    @SerializedName("modifiedAt")
    @Expose
    private String modifiedAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getModifiedt() {
        return modifiedt;
    }

    public void setModifiedt(Object modifiedt) {
        this.modifiedt = modifiedt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

}
