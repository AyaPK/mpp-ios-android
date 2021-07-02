package com.jetbrains.handson.mpp.mobile

import com.jetbrains.handson.mpp.mobile.models.DisplayStation
import com.jetbrains.handson.mpp.mobile.models.OutboundJourneys
import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface View {
        fun setLabel(text: String)
        fun updateResults(data: List<OutboundJourneys>)
        fun updateDisplayStations(stations: ArrayList<DisplayStation>)
    }

    abstract class Presenter : CoroutineScope {
        abstract fun onViewTaken(view: View)
        abstract fun requestFromAPI(departureCode: String, arrivalCode: String, currentDateAndTime: String)
        abstract fun requestStationsFromAPI()
        }
}
