package com.example.karma.interfaces

import com.example.karma.response.getOrderListByUserId.ProductList
import com.example.karma.response.getOrderListForAdminByAppIdVendorId.OrderList
import com.example.karma.response.getOrderListForAdminByAppIdVendorId.Result

interface OrderItemClickListner {
    fun OrderItemClick(order: ProductList)

}