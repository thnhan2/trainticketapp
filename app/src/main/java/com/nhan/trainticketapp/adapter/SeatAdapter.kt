package com.nhan.trainticketapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nhan.trainticketapp.R
import com.nhan.trainticketapp.model.Seat


class SeatAdapter(private val seats: List<Seat>) : RecyclerView.Adapter<SeatAdapter.ViewHolder>() {
    private val selectedSeats = mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.seat_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val seat = seats[position]
        holder.itemView.setOnClickListener {
            if (seat.position[position]) {
                holder.itemView.setBackgroundColor(Color.GRAY)
                holder.itemView.isClickable = false
            } else {
                holder.itemView.setBackgroundColor(Color.GREEN)
                selectedSeats.add(position)
            }
        }
    }

    override fun getItemCount() = seats.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
