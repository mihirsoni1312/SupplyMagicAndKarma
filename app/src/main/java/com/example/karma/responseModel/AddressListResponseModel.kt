package com.example.karma.responseModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddressListResponseModel(

    @SerializedName("addressId")
    var addressId: String,
    @SerializedName("address")
    var address: String,
    @SerializedName("zipCode")
    var zipCode: String,
    @SerializedName("isPrimary")
    var isPrimary: Boolean,
    @SerializedName("isActive")
    var isActive: Boolean,
    @SerializedName("isHomeAddress")
    var isHomeAddress: Boolean

) : Serializable