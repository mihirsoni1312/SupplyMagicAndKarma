package com.example.karma.interfaces

import com.example.karma.response.PCK

interface CardDeleteClickListner {

    fun cartDelete(cardId: String, paymenttype: String, bankdetails: PCK)
}