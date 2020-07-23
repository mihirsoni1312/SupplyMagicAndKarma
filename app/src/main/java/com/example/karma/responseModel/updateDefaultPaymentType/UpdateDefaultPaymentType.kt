package com.example.karma.responseModel.updateDefaultPaymentType

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UpdateDefaultPaymentType {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("responseCode")
    @Expose
    var responseCode: Int? = null

    @SerializedName("result")
    @Expose
    var result: Result? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

}