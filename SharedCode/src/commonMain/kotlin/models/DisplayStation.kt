package com.jetbrains.handson.mpp.mobile.models

import kotlinx.serialization.Serializable

@Serializable
data class DisplayStation(
        val name: String,
        val crs: String?
)

