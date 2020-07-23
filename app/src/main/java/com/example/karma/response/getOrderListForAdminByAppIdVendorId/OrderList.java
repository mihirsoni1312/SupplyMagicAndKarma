package com.example.karma.response.getOrderListForAdminByAppIdVendorId;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderList {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("invoiceId")
    @Expose
    private String invoiceId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("orderDate")
    @Expose
    private String orderDate;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("zipCode")
    @Expose
    private String zipCode;
    @SerializedName("orderTotalAmount")
    @Expose
    private String orderTotalAmount;
    @SerializedName("payableAmount")
    @Expose
    private String payableAmount;
    @SerializedName("orderTimeSlot")
    @Expose
    private OrderTimeSlot orderTimeSlot;
    @SerializedName("status")
    @Expose
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public void setOrderTotalAmount(String orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }

    public String getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(String payableAmount) {
        this.payableAmount = payableAmount;
    }

    public OrderTimeSlot getOrderTimeSlot() {
        return orderTimeSlot;
    }

    public void setOrderTimeSlot(OrderTimeSlot orderTimeSlot) {
        this.orderTimeSlot = orderTimeSlot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
