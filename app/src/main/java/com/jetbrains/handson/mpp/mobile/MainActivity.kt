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
import com.jetbrains.handson.mpp.mobile.models.OriginStation
import com.jetbrains.handson.mpp.mobile.models.OutboundJourneys
import java.text.SimpleDateFormat
import java.util.*


class RecycleAdapter(private val journeyList: ArrayList<OutboundJourneys>) : RecyclerView.Adapter<RecycleAdapter.ViewHolder>() {
    // holder class to hold reference
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //get view reference
        var stationName: TextView = view.findViewById(R.id.station_name) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create view holder to hold reference
        return ViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //set values
        val journey = journeyList[position]
        holder.stationName.text =  journey.originStation.displayName + " > " + journey.destinationStation.displayName + " At " + journey.departureTime.split("T")[1].split("+")[0].split(".")[0]
    }

    override fun getItemCount(): Int {
        return journeyList.size
    }
    // update your data
    fun updateData(scanResult: List<OutboundJourneys>) {
        journeyList.clear()
        notifyDataSetChanged()
        journeyList.addAll(scanResult)
        notifyDataSetChanged()
    }
}

class MainActivity : AppCompatActivity(), ApplicationContract.View {
    lateinit var departureStationSelected: Spinner
    lateinit var arrivalStationSelected: Spinner
    lateinit var recyclerView: RecyclerView
    lateinit var recycleAdapter: RecycleAdapter
    private val presenter = ApplicationPresenter()


    lateinit var outboundJourneys : List <OutboundJourneys>

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
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recycleAdapter
    }

    override fun setLabel(text: String) {
        findViewById<TextView>(R.id.main_text).text = text
    }

    fun onSubmitButtonTapped(view: View) {
        val departureCode : String = departureStationSelected.selectedItem.toString().split(" ").last().replace("[", "").replace("]", "");
        val arrivalCode : String = arrivalStationSelected.selectedItem.toString().split(" ").last().replace("[", "").replace("]", "");

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'at'HH:mm:ss z")
        val currentDateAndTime: String = simpleDateFormat.format(Date())

        presenter.requestFromAPI(departureCode, arrivalCode)

//        val url = "https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/$departureCode/$arrivalCode/#LiveDepResults"
//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.data = Uri.parse(url)
//        startActivity(intent)
    }

    override fun updateResults(data: List<OutboundJourneys>){
        var x = data
        this.recycleAdapter.updateData(data)
    }


}
