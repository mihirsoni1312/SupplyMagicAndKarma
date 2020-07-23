
package com.example.karma.response.getOrderListForAdminByAppIdVendorId;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

    @SerializedName("orderCount")
    @Expose
    private Integer orderCount;
    @SerializedName("currentPage")
    @Expose
    private Integer currentPage;
    @SerializedName("totalOrders")
    @Expose
    private Integer totalOrders;
    @SerializedName("hasMoreOrders")
    @Expose
    private Boolean hasMoreOrders;
    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("orderList")
    @Expose
    private List<OrderList> orderList = null;

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Boolean getHasMoreOrders() {
        return hasMoreOrders;
    }

    public void setHasMoreOrders(Boolean hasMoreOrders) {
        this.hasMoreOrders = hasMoreOrders;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<OrderList> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderList> orderList) {
        this.orderList = orderList;
    }

}
