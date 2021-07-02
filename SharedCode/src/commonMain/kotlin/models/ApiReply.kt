package com.jetbrains.handson.mpp.mobile.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiReply(
        val outboundJourneys: List<OutboundJourneys>
)