package com.example.karma.interfaces

import com.example.karma.responseModel.AddressListResponseModel

interface AddressClickListner {

    fun editAddress(addressListResponseModel: AddressListResponseModel, position: Int)
}