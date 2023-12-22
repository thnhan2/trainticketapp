package com.nhan.trainticketapp.fragment
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//import com.nhan.trainticketapp.R
//import com.nhan.trainticketapp.adapter.TicketAdapter
//import com.nhan.trainticketapp.databinding.FragmentActivityBinding
//import com.nhan.trainticketapp.model.Ticket
//import com.nhan.trainticketapp.model.Train
//import com.nhan.trainticketapp.service.TrainService
//
//class com.nhan.trainticketapp.fragment.ActivityFragment : Fragment() {
//
//    private lateinit var binding: FragmentActivityBinding
//    private var ticketList: MutableList<Ticket> = mutableListOf()
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var ticketAdapter: TicketAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        fetchData()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentActivityBinding.inflate(inflater, container, false)
//        recyclerView = binding.root.findViewById(R.id.recyclerView)
//        ticketAdapter = TicketAdapter(ticketList)
//
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = ticketAdapter
//
//        println("out size$ticketList")
//
//        return binding.root
//    }
//
//
//
////
////    private fun fetchData() {
////        val database = FirebaseDatabase.getInstance("https://trainticket-19d0e-default-rtdb.asia-southeast1.firebasedatabase.app")
////        val objectsRef = database.getReference("ticket")
////
////        val userId = FirebaseAuth.getInstance().uid
////
////        objectsRef.addValueEventListener(object : ValueEventListener {
////            override fun onDataChange(dataSnapshot: DataSnapshot) {
////                val tempList: MutableList<Ticket> = mutableListOf()
////                for (childSnapshot in dataSnapshot.children) {
////                    val firebaseObject = childSnapshot.getValue(Ticket::class.java)
////                    if (firebaseObject?.user_id == userId) {
////                        tempList.add(firebaseObject!!)
////                    }
////                }
////                ticketList = tempList
////                activity?.runOnUiThread {
////                    ticketAdapter = TicketAdapter(ticketList)
////                    recyclerView.adapter = ticketAdapter
////                }
////
////                // In danh sách các đối tượng
////                ticketList.forEach { obj ->
////                    println("Seat: ${obj.seat}, Train ID: ${obj.train_id}, User ID: ${obj.user_id}")
////                }
////            }
////
////            override fun onCancelled(databaseError: DatabaseError) {
////                println("Error: ${databaseError.message}")
////            }
////        })
////    }
//
//}
