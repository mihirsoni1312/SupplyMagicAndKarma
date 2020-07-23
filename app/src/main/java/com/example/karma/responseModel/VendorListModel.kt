package com.example.karma.responseModel

import com.example.karma.response.PCK
import com.google.gson.annotations.SerializedName

data class VendorListModel(

    @SerializedName("vendorId")
    var vendorId: String,

    @SerializedName("appName")
    var appName: String,

    @SerializedName("add")
    var add: String,

    @SerializedName("zip")
    var zip: String,

    @SerializedName("phone")
    var phone: String,

    @SerializedName("logo")
    var logo: String,

    @SerializedName("plpf")
    var plpf: String,
    @SerializedName("plpk")
    var plpk: String,
    @SerializedName("pck")
    var pck: List<PCK>,
    @SerializedName("sck")
    var sck: String,
    @SerializedName("plck")
    var plck: String,
    @SerializedName("defaultPaymentType")
    var defaultPaymentType: String,
    @SerializedName("defaultTypeBank")
    var defaultTypeBank: PCK,
    @SerializedName("paymentTypeName")
    var paymentTypeName: String,
    @SerializedName("isStopOrder")
    var isStopOrder: Boolean


)