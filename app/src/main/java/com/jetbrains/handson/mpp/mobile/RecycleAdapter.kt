package com.jetbrains.handson.mpp.mobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jetbrains.handson.mpp.mobile.models.OutboundJourneys
import java.util.ArrayList

class RecycleAdapter(private val journeyList: ArrayList<OutboundJourneys>) : RecyclerView.Adapter<RecycleAdapter.ViewHolder>() {
    // holder class to hold reference
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //get view reference
        var stationName: TextView = view.findViewById(R.id.station_name) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create view holder to hold reference
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //set values
        val journey = journeyList[position]
        holder.stationName.text = journey.originStation.displayName + " > " + journey.destinationStation.displayName + " At " + journey.departureTime.split("T")[1].split("+")[0].split(".")[0]
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