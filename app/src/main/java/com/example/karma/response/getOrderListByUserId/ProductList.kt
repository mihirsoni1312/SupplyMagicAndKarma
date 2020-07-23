package com.example.karma.response.getOrderListByUserId

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductList {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("invoiceId")
    @Expose
    var invoiceId: String? = null

    @SerializedName("orderDate")
    @Expose
    var orderDate: String? = null

    @SerializedName("orderTotalAmount")
    @Expose
    var orderTotalAmount: String? = null

    @SerializedName("orderDiscount")
    @Expose
    var orderDiscount: String? = null

    @SerializedName("payableAmount")
    @Expose
    var payableAmount: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("systemDate")
    @Expose
    var systemDate: SystemDate? = null

    @SerializedName("oD")
    @Expose
    var oD: String? = null

    @SerializedName("tAmt")
    @Expose
    var tAmt: String? = null

    @SerializedName("pAmt")
    @Expose
    var pAmt: String? = null

    @SerializedName("dAmt")
    @Expose
    var dAmt: String? = null

}