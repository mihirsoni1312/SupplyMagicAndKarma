
package com.example.karma.response.getOrderListForAdminByAppIdVendorId;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetOrderListForAdminByAppIdVendorIdResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("result")
    @Expose
    private Result result = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

}
