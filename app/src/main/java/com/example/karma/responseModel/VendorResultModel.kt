package com.example.karma.responseModel

import com.google.gson.annotations.SerializedName

data class VendorResultModel (

    @SerializedName("totalVendors")
    var totalVendors: Int,
    @SerializedName("vendorList")
    var vendorList: ArrayList<VendorListModel>


)