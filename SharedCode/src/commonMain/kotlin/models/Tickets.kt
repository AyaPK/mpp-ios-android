package com.jetbrains.handson.mpp.mobile.models

import kotlinx.serialization.Serializable

@Serializable
data class Tickets(
        val name: String,
        val priceInPennies: Int

)