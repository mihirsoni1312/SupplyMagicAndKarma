package com.example.karma.responseModel

import com.google.gson.annotations.SerializedName

data class ResultResponseModel(

    @SerializedName("sliderInfo")
    var sliderInfo: ArrayList<SliderInfoModel>,
    @SerializedName("categories")
    var categories: ArrayList<CategoryModel>


)