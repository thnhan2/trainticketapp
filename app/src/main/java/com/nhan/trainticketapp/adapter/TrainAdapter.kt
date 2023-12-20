package com.nhan.trainticketapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nhan.trainticketapp.R
import com.nhan.trainticketapp.model.Train

class TrainAdapter(private val trainList: List<Train>,
    private val onItemClick: (Train) -> Unit):
    RecyclerView.Adapter<TrainAdapter.TrainViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_train, parent, false)
        return TrainViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrainViewHolder, position: Int) {
        val currentTrain = trainList[position]

        val timeDepart:String = currentTrain.schedules[0].departure_time
        holder.tvTrainStart.text = timeDepart
        val duration = convertMinutesToTimeString(currentTrain.duration)
        holder.tvDuration.text = duration
        val (newTime, newDay) = addTime(timeDepart, duration)
        var endTime = ""

        endTime = if (newDay >0 ) {
            "$newTime (+$newDay days)"
        } else {
            newTime
        }

        holder.tvTrainEnd.text = newTime
        holder.tvOrigin.text = currentTrain.origin
        holder.tvDestination.text = currentTrain.destination
        holder.tvDescription.text = currentTrain.description
        holder.tvDateDeparture.text = currentTrain.schedules[0].departure_date

        holder.itemView.setOnClickListener {
            onItemClick(currentTrain)
        }
    }

    private fun addTime(time: String, duration: String): Pair<String, Int> {
        val hour = time.substringBefore(":").toInt()
        val minute = time.substringAfter(":").toInt()

        val addHour = duration.substringBefore("h").toInt()
        val addMinute = duration.substringAfter("h").substringBefore("m").toInt()

        val newMinute = minute + addMinute
        val newHour = hour + addHour + newMinute / 60
        val newDay = newHour / 24

        val formattedTime = String.format("%02d:%02d", newHour % 24, newMinute % 60)

        return Pair(formattedTime, newDay)
    }


    private fun convertMinutesToTimeString(minutes: Int): String {
        val hours = minutes / 60
        val minutesInHour = minutes % 60
        return "${hours}h${minutesInHour}m"
    }

    override fun getItemCount(): Int {
        return trainList.size
    }

    inner class TrainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTrainStart: TextView = itemView.findViewById(R.id.tvTrainStart) // time train start
        val tvOrigin: TextView = itemView.findViewById(R.id.tvOrigin) // origin station
        val tvTrainEnd: TextView = itemView.findViewById(R.id.tvTrainEnd) // time train end
        val tvDestination: TextView = itemView.findViewById(R.id.tvDestination) // destination station
        val tvDuration: TextView = itemView.findViewById(R.id.tvDuration) // time duration
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription) // route description
        val tvDateDeparture: TextView = itemView.findViewById(R.id.tvDateDeparture) // date start




    }
    }