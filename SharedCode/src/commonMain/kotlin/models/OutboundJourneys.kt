package com.jetbrains.handson.mpp.mobile.models

import kotlinx.serialization.Serializable

@Serializable
data class OutboundJourneys(
        val originStation: Station,
        val destinationStation: Station,
        val tickets: List<Tickets>,
        val departureTime: String,
        val arrivalTime: String
)