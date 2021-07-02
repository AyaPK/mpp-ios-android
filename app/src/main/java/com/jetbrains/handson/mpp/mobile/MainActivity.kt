package com.jetbrains.handson.mpp.mobile


import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jetbrains.handson.mpp.mobile.models.DisplayStation
import com.jetbrains.handson.mpp.mobile.models.OutboundJourneys
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), ApplicationContract.View {
    lateinit var departureStationSelected: Spinner
    lateinit var arrivalStationSelected: Spinner
    lateinit var recyclerView: RecyclerView
    lateinit var recycleAdapter: RecycleAdapter
    var stationsHashMap: HashMap<String, String> = HashMap<String, String>()
    private val presenter = ApplicationPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.onViewTaken(this)

        presenter.requestStationsFromAPI()

        departureStationSelected = findViewById<Spinner>(R.id.station_names_spinner_departure) as Spinner
        arrivalStationSelected = findViewById<Spinner>(R.id.station_names_spinner_arrival) as Spinner
        departureStationSelected.setSelection(0)
        arrivalStationSelected.setSelection(1)

        recyclerView = findViewById(R.id.journeyList) as RecyclerView
        recycleAdapter = RecycleAdapter(ArrayList())
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = recycleAdapter
    }

    override fun setLabel(text: String) {
        findViewById<TextView>(R.id.main_text).text = text
    }

    fun onSubmitButtonTapped(view: View) {
        //val departureCode: String = departureStationSelected.selectedItem.toString().split(" ").last().replace("[", "").replace("]", "");
        //val arrivalCode: String = arrivalStationSelected.selectedItem.toString().split(" ").last().replace("[", "").replace("]", "");
        val departureCode: String = stationsHashMap[departureStationSelected.selectedItem.toString()] as String
        val arrivalCode: String = stationsHashMap[arrivalStationSelected.selectedItem.toString()] as String

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH'%3A'mm")
        val date = Date()
        date.time = date.time.plus(60000)
        val currentDateAndTime: String = simpleDateFormat.format(date)

        presenter.requestFromAPI(departureCode, arrivalCode, currentDateAndTime)
    }

    override fun updateResults(data: List<OutboundJourneys>) {
        this.recycleAdapter.updateData(data)
    }

    override fun updateDisplayStations(stations: ArrayList<DisplayStation>) {
        // SPINNER XML IS UPDATED
        for (station in stations) {
            this.stationsHashMap[station.name] = station.crs as String
        }
        val res: Resources = resources
        val stationsArray = res.getStringArray(R.array.stations_array)
        val sortedStations = stationsHashMap.toSortedMap()
        val dSpinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1)
        val aSpinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1)
        dSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        aSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        departureStationSelected.setAdapter(dSpinnerAdapter)
        arrivalStationSelected.setAdapter(aSpinnerAdapter)

        for (station in sortedStations) {
            dSpinnerAdapter.add(station.key)
            aSpinnerAdapter.add(station.key)
        }

        dSpinnerAdapter.notifyDataSetChanged()
        aSpinnerAdapter.notifyDataSetChanged()
    }
}
