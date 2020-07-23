package com.example.karma.interfaces

import com.example.karma.responseModel.AddressListResponseModel

interface AddressSelectClickListner {
    fun click(addressListResponseModel: AddressListResponseModel)
}