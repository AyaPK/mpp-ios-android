package com.jetbrains.handson.mpp.mobile.models

import kotlinx.serialization.Serializable

@Serializable
data class StationsReply(
        var stations: List<DisplayStation>
)