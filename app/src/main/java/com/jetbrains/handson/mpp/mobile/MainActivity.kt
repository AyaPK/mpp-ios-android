package com.jetbrains.handson.mpp.mobile


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jetbrains.handson.mpp.mobile.models.OutboundJourneys
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), ApplicationContract.View {
    lateinit var departureStationSelected: Spinner
    lateinit var arrivalStationSelected: Spinner
    lateinit var recyclerView: RecyclerView
    lateinit var recycleAdapter: RecycleAdapter
    private val presenter = ApplicationPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.onViewTaken(this)

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
        val departureCode: String = departureStationSelected.selectedItem.toString().split(" ").last().replace("[", "").replace("]", "");
        val arrivalCode: String = arrivalStationSelected.selectedItem.toString().split(" ").last().replace("[", "").replace("]", "");

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH'%3A'mm")
        val date = Date()
        date.time = date.time.plus(60000)
        val currentDateAndTime: String = simpleDateFormat.format(date)

        presenter.requestFromAPI(departureCode, arrivalCode, currentDateAndTime)
    }

    override fun updateResults(data: List<OutboundJourneys>) {
        this.recycleAdapter.updateData(data)
    }


}
