package com.jetbrains.handson.mpp.mobile

import com.jetbrains.handson.mpp.mobile.models.*
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext


class ApplicationPresenter : ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private var view: ApplicationContract.View? = null
    private val job: Job = SupervisorJob()

    private lateinit var outboundJourneys: List<OutboundJourneys>

    private val client = HttpClient() {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
            })
        }
    }

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
    }

    fun showLabel(string: String) {
        this.view?.setLabel(string)
    }

    fun updateDisplayStations(stations: ArrayList<DisplayStation>){
        this.view?.updateDisplayStations(stations)
    }

    override fun requestStationsFromAPI() {
        launch {
            val response: StationsReply = client.get(
                    "https://mobile-api-softwire1.lner.co.uk/v1/stations"
            )
            val stations : ArrayList<DisplayStation> = ArrayList<DisplayStation>();
            for(station in response.stations){
                if (station.crs != null) {
                    stations.add(station)
                }
            }
        updateDisplayStations(stations);
        }
    }

    override fun requestFromAPI(departureCode: String, arrivalCode: String, currentDateAndTime: String) {
        if (departureCode != arrivalCode) {
            showLabel("Loading results...")
            launch {
                val response: ApiReply = client.get(
                    "https://mobile-api-softwire1.lner.co.uk/v1/fares?" +
                            "originStation=$departureCode" +
                            "&destinationStation=$arrivalCode" +
                            "&noChanges=false" +
                            "&numberOfAdults=1" +
                            "&numberOfChildren=0" +
                            "&journeyType=single" +
                            "&outboundDateTime=" + currentDateAndTime + "%3A00.000%2B01%3A00" +
                            "&outboundIsArriveBy=false"
                )
                updateResultsTable(response.outboundJourneys)
            }
        } else {
            showLabel("Please choose two different stations.")
        }
    }

    fun updateResultsTable(data: List<OutboundJourneys>) {
        this.view?.updateResults(data)
        if (data.size > 0) {
            this.view?.setLabel("")
        } else {
            this.view?.setLabel("No trains found!")
        }
    }
}
