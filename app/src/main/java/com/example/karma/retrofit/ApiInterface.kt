package com.example.karma.retrofit

import com.example.karma.response.*
import com.example.karma.response.deliverySlotResponse.DeliverySlotResponse
import com.example.karma.response.getOrderListByUserId.GetOrderListByUserId
import com.example.karma.response.getOrderListForAdminByAppIdVendorId.GetOrderListForAdminByAppIdVendorIdResponse
import com.example.karma.response.insertOrder.InsertOrder
import com.example.karma.response.updateUserByIdResponse.UpdateUserByIdResponse
import com.example.karma.response.userImage.UserImage
import com.example.karma.responseModel.getPlaidToken.GetPlaidToken
import com.example.karma.responseModel.orderDetailResponse.OrderDetailResponse
import com.example.karma.responseModel.updateDefaultPaymentType.UpdateDefaultPaymentType
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("registration")
    fun registration(
        @Body body: JsonObject
    ): Call<CommonResponse>

    @POST("getMobileAppConfigByAppId")
    fun getAppConfigByAppId(
        @Body body: JsonObject
    ): Call<ConfigAppIdResponse>

    @POST("login")
    fun login(
        @Body body: JsonObject
    ): Call<LoginResponse>

    @POST("homePageInfo")
    fun homePageInfo(
        @Body body: JsonObject
    ): Call<HomeScreenResponse>

    @POST("getAddressByUserId")
    fun getAddressByUserId(
        @Body body: JsonObject
    ): Call<GetAddressListResponse>

    @POST("addAddressByUserId")
    fun addAddressByUserId(
        @Body body: JsonObject
    ): Call<AddAddressResponse>

    @POST("updateAddressByAddressId")
    fun updateAddressByAddressId(
        @Body body: JsonObject
    ): Call<AddAddressResponse>

    @POST("changePassword")
    fun changePassword(
        @Body body: JsonObject
    ): Call<ChangePasswordResponse>

    @POST("getCMSByAppIdVendorIdTypeId")
    fun cmsData(
        @Body body: JsonObject
    ): Call<CMSResponse>

    @POST("productByCategoryId")
    fun productListByCategoryId(
        @Body body: JsonObject
    ): Call<ProductByCategoryIdResponse>

    @POST("productByProductId")
    fun productByProductId(
        @Body body: JsonObject
    ): Call<ProductByProductIdResponse>

    @POST("addToCart")
    fun addToCart(
        @Body body: JsonObject
    ): Call<CommonSimpleResponse>

    @POST("addToWishList")
    fun addToWishList(
        @Body body: JsonObject
    ): Call<CommonSimpleResponse>

    @POST("getCartInfo")
    fun getCartInfo(
        @Body body: JsonObject
    ): Call<CartListResponse>

    @POST("getWishListInfo")
    fun getWishListInfo(
        @Body body: JsonObject
    ): Call<GetWishListResponse>

    @POST("getAssociatedVendorList")
    fun getAssociatedVendorList(
        @Body body: JsonObject
    ): Call<VendorListResponse>

    @POST("removeFromCart")
    fun removeFromCart(
        @Body body: JsonObject
    ): Call<CommonSimpleResponse>

    @POST("removeFromWishList")
    fun removeFromWishList(
        @Body body: JsonObject
    ): Call<CommonSimpleResponse>

    @POST("searchProducts")
    fun searchProducts(
        @Body body: JsonObject
    ): Call<SearchResponse>

    @POST("CCL")
    fun cardList(
        @Body body: JsonObject
    ): Call<PaymentResponse>
    @POST("getPlaidToken")
    fun getPlaidToken(
        @Body body: JsonObject
    ): Call<GetPlaidToken>

    @POST("CCD")
    fun cardDelete(
        @Body body: JsonObject
    ): Call<Deletecartresponse>

    @POST("updateDefaultPaymentType")
    fun updateDefaultPaymentType(
        @Body body: JsonObject
    ): Call<UpdateDefaultPaymentType>

    @POST("CCS")
    fun createUserStripe(
        @Body body: JsonObject
    ): Call<CreateUserStripeResponse>

    @POST("updateCustomerKeys")
    fun updateCustomerKeys(
        @Body body: JsonObject
    ): Call<UpdateCustomerKeyResponse>

    @POST("DeliverySlot")
    fun getDeliverySlot(
        @Body body: JsonObject
    ): Call<DeliverySlotResponse>

    @POST("getOrderListByUserId")
    fun getOrderListByUserId(
        @Body body: JsonObject
    ): Call<GetOrderListByUserId>

    @POST("insertCustomerRequest")
    fun Healp(
        @Body body: JsonObject
    ): Call<Helplist>


    @POST("updateUserById")
    fun updateUserById(
        @Body body: JsonObject
    ): Call<UpdateUserByIdResponse>

    @POST("insertOrder")
    fun insertOrder(
        @Body body: JsonObject
    ): Call<InsertOrder>

    @POST("SendEmail")
    fun SendEmail(
        @Body body: JsonObject
    ): Call<CommonResponse>

    @POST("UserImage")
    fun UserImage(
        @Body body: JsonObject
    ): Call<UserImage>

    @POST("validatePromoCode ")
    fun validatePromoCode (
        @Body body: JsonObject
    ): Call<Promocoderesponse>

 @POST("getOrderDetailByOrderId ")
    fun getOrderDetailByOrderId (
        @Body body: JsonObject
    ): Call<OrderDetailResponse>

 @POST("editCart")
    fun editCart (
        @Body body: JsonObject
    ): Call<CommonSimpleResponse>

    @POST("saveCheckoutOrder")
    fun saveCheckoutOrder (
        @Body body: JsonObject
    ): Call<CommonSimpleResponse>

}