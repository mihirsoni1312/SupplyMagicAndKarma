package com.example.karma.responseModel

import com.example.karma.response.PCK
import com.google.gson.annotations.SerializedName

data class PaymentCardModel(


//{
//    "id": "3",
//    "Exp": "",
//    "Last4": "",
//    "Brand": "Cash On Delivery",
//    "isDefault": false,
//    "Type": "COD",
//    "BankDetails": {
//    "bId": "",
//    "cId": ""
//}


    @SerializedName("id")
    var id: String,

    @SerializedName("Exp")
    var Exp: String,
    @SerializedName("Last4")
    var Last4: String,

    @SerializedName("Brand")
    var Brand: String,
    @SerializedName("isDefault")
    var isDefault: Boolean,
    @SerializedName("Type")
    var Type: String,
    @SerializedName("BankDetails")
    var BankDetails: PCK



)