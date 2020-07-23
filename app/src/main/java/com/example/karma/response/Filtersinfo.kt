package com.example.karma.response

import com.example.karma.responseModel.WishListProductListModel
import com.google.gson.annotations.SerializedName

data class Filtersinfo(

    @SerializedName("category")
    var category: ArrayList<Category>

)