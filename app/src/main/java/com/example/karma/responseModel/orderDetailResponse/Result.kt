package com.example.karma.responseModel.orderDetailResponse

import com.example.karma.response.PCK
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("vendorId")
    @Expose
    var vendorId: String? = null

    @SerializedName("appId")
    @Expose
    var appId: String? = null

    @SerializedName("orderNo")
    @Expose
    var orderNo: String? = null

    @SerializedName("invoiceId")
    @Expose
    var invoiceId: String? = null

    @SerializedName("cartId")
    @Expose
    var cartId: String? = null

    @SerializedName("userId")
    @Expose
    var userId: String? = null

    @SerializedName("orderType")
    @Expose
    var orderType: String? = null

    @SerializedName("deliveryCharge")
    @Expose
    var deliveryCharge: String? = null

    @SerializedName("userName")
    @Expose
    var userName: String? = null

    @SerializedName("mobileNumber")
    @Expose
    var mobileNumber: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("isDineInOrder")
    @Expose
    var isDineInOrder: Boolean? = null

    @SerializedName("isAllowEdit")
    @Expose
    var isAllowEdit: Boolean? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("zipCode")
    @Expose
    var zipCode: String? = null

    @SerializedName("vendorInfo")
    @Expose
    var vendorInfo: VendorInfo? = null

    @SerializedName("paymentType")
    @Expose
    var paymentType: String? = null

    @SerializedName("paymentTypeId")
    @Expose
    var paymentTypeId: PCK? = null

    @SerializedName("cartAllowTime")
    @Expose
    var cartAllowTime: String? = null

    @SerializedName("totalProductTax")
    @Expose
    var totalProductTax: String? = null

    @SerializedName("genralDiscount")
    @Expose
    var genralDiscount: String? = null

    @SerializedName("promoCode")
    @Expose
    var promoCode: PromoCode? = null

    @SerializedName("orderComments")
    @Expose
    var orderComments: List<Any>? = null

    @SerializedName("orderTimeSlot")
    @Expose
    var orderTimeSlot: OrderTimeSlot? =
        null

    @SerializedName("paymentConfirmationId")
    @Expose
    var paymentConfirmationId: String? = null

    @SerializedName("paymentResponse")
    @Expose
    var paymentResponse: String? = null

    @SerializedName("paymentStatus")
    @Expose
    var paymentStatus: String? = null

    @SerializedName("tip")
    @Expose
    var tip: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

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

    @SerializedName("productInfo")
    @Expose
    var productInfo: List<ProductInfo>? = null

}