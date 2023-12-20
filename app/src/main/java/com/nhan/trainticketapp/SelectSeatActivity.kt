package com.nhan.trainticketapp

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.nhan.trainticketapp.adapter.SeatAdapter
import com.nhan.trainticketapp.model.Seat

class SelectSeatActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SeatAdapter
    private val seats = mutableListOf<Seat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_seat)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = SeatAdapter(seats)
        recyclerView.adapter = adapter

        // Fetch data from Firebase and update the SeatAdapter
        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase() {
        val database = FirebaseDatabase.getInstance("https://trainticket-19d0e-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("seat")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val trainId = snapshot.child("train_id").value as Long
                    if (trainId == 1L) {
                        val position = snapshot.child("position").value as List<Boolean>
                        val seat = Seat(position, trainId.toInt())
                        seats.add(seat)
                        println(seats)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
            }
        })
    }
}
