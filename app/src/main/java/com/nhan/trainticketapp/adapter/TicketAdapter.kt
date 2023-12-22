package com.nhan.trainticketapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nhan.trainticketapp.R
import com.nhan.trainticketapp.model.Ticket

class TicketAdapter(private val ticketList: List<Ticket>, private val listener: OnItemClickListener) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    interface OnItemClickListener {
        fun onDetailClick(ticket: Ticket)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ticket_item_layout, parent, false)
        return TicketViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = ticketList[position]
        holder.bind(ticket)
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }

    inner class TicketViewHolder(itemView: View, private val listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        private val seatTextView: TextView = itemView.findViewById(R.id.tvSeat)
        private val trainIdTextView: TextView = itemView.findViewById(R.id.tvTrainID)
        private val userIdTextView: TextView = itemView.findViewById(R.id.tvUserId)
        private val ticketNoTextView: TextView = itemView.findViewById(R.id.tvTicketNo)
        private val buttonDetail: Button = itemView.findViewById(R.id.btnDetail)

        fun bind(ticket: Ticket) {
            seatTextView.text = "Seat: ${ticket.seat+1}"
            trainIdTextView.text = "Train ID: ${ticket.train_id}"
            userIdTextView.text = "User ID: ${ticket.user_id}"
            ticketNoTextView.text = "Ticket No: ${ticket.train_id}${ticket.user_id}"
            buttonDetail.text = "Detail"
            buttonDetail.setOnClickListener { listener.onDetailClick(ticket) }
        }
    }
}
