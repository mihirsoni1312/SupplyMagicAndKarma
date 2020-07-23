package com.example.karma.responseModel

import com.google.gson.annotations.SerializedName

data class GetAddressResultResponseModel(

    @SerializedName("userId")
    var userId: String,
    @SerializedName("appId")
    var appId: String,

    @SerializedName("addressCount")
    var addressCount: Int,

    @SerializedName("addressList")
    var addressList: ArrayList<AddressListResponseModel>


)