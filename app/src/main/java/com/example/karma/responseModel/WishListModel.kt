package com.example.karma.responseModel

import com.example.karma.response.Filtersinfo
import com.google.gson.annotations.SerializedName

data class WishListModel(

    @SerializedName("wishlistId")
    var wishlistId: String,

    @SerializedName("productList")
    var productList: ArrayList<WishListProductListModel>,

    @SerializedName("currentPage")
    var currentPage: String,

    @SerializedName("totalProducts")
    var totalProducts: String,

    @SerializedName("hasMoreProducts")
    var hasMoreProducts: String,

    @SerializedName("totalPages")
    var totalPages: String,

    @SerializedName("filters")
    var filters: Filtersinfo
)