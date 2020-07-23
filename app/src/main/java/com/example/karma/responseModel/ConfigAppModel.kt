package com.example.karma.responseModel

import com.google.gson.annotations.SerializedName

data class ConfigAppModel(

    @SerializedName("_id")
    var _id: String,

    @SerializedName("appName")
    var appName: String,

    @SerializedName("appId")
    var appId: String,

    @SerializedName("backgroundColor")
    var backgroundColor: String,

    @SerializedName("fontColor")
    var fontColor: String,

    @SerializedName("buttonFontColor")
    var buttonFontColor: String,

    @SerializedName("loginScreenLogo")
    var loginScreeLogo: String,

    @SerializedName("registrationScreenLogo")
    var registrationScreenLogo: String,

    @SerializedName("slideMenuBackGroundColor")
    var slideMenuBackGroundColor: String,
    @SerializedName("defaultVendorId")
    var defaultVendorId: String,
    @SerializedName("slideMenuFontColor")
    var slideMenuFontColor: String,
    @SerializedName("slideMenuIconColor")
    var slideMenuIconColor: String,
    @SerializedName("isRestaurentApp")
    var isRestaurentApp: Boolean,
    @SerializedName("isActive")
    var isActive: Boolean


)