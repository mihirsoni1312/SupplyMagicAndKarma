package com.example.karma.response.getOrderListByUserId

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result {
    @SerializedName("ordersCount")
    @Expose
    var ordersCount: Int? = null

    @SerializedName("currentPage")
    @Expose
    var currentPage: Int? = null

    @SerializedName("totalOrders")
    @Expose
    var totalOrders: Int? = null

    @SerializedName("hasMoreOrders")
    @Expose
    var hasMoreOrders: Boolean? = null

    @SerializedName("totalPages")
    @Expose
    var totalPages: Int? = null

    @SerializedName("productList")
    @Expose
    var productList: List<ProductList>? = null

}