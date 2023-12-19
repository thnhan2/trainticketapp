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
        // init view value
        holder.tvTrainName.text = currentTrain.train_id.toString()
        holder.tvTrainDescription.text = currentTrain.description
        val trainName = currentTrain.origin + " -> " + currentTrain.destination
        holder.tvTrainName.text = trainName
        holder.tvTimeStart.text = currentTrain.schedules[0].departure_time
        holder.tvDateStart.text = currentTrain.schedules[0].departure_date
        var hours: Int = currentTrain.duration

        holder.tvDuration.text = convertMinutesToTimeString(hours)
        // Gán các giá trị khác vào các TextView khác nếu cần

        holder.itemView.setOnClickListener {
            onItemClick(currentTrain)
        }
    }

    fun convertMinutesToTimeString(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60

        val hoursString = if (hours > 0) "${hours}h " else ""
        val minutesString = if (remainingMinutes > 0) "${remainingMinutes}min" else ""

        return hoursString + minutesString
    }
    override fun getItemCount(): Int {
        return trainList.size
    }

    inner class TrainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTrainName: TextView = itemView.findViewById(R.id.tvTrainName)
        val tvTrainDescription: TextView = itemView.findViewById(R.id.tvRemaining)
        val tvTimeStart: TextView = itemView.findViewById(R.id.tvTrainStart)
        val tvDateStart: TextView = itemView.findViewById(R.id.tvTrainDateStart)
        val tvDuration: TextView = itemView.findViewById(R.id.tvTrainDuration)




    }
    }