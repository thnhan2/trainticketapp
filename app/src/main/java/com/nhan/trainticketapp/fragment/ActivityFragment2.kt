package com.nhan.trainticketapp.fragment
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//import com.nhan.trainticketapp.R
//import com.nhan.trainticketapp.model.Train
//
///**
// * A simple [Fragment] subclass.
// * Use the [ActivityFragment2.newInstance] factory method to
// * create an instance of this fragment.
// */
//class ActivityFragment2 : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//
//        // fetch
//        val database =
//            FirebaseDatabase.getInstance("https://trainticket-19d0e-default-rtdb.asia-southeast1.firebasedatabase.app")
//        val ref: DatabaseReference = database.getReference("/train/train1")
//        println(ref == null)
//        ref.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val trainData = snapshot.getValue(Train::class.java)
//
//                if (trainData != null) {
//                    val description = trainData.description
//                    val destination = trainData.destination
//                    val duration = trainData.duration
//                    val origin = trainData.origin
//                    val schedules = trainData.schedules
//                    val stations = trainData.stations
//                    val trainId = trainData.trainId
//
//                    // Log the values of the data
//                    println("TrainData Description: $description")
//                    println("TrainData Destination: $destination")
//                    println("TrainData Duration: $duration")
//                    println("TrainData Origin: $origin")
//                    println("TrainData Schedules: $schedules")
//                    println("TrainData Stations: $stations")
//                    println("TrainData Train ID: $trainId")
//                    println("schedule -----")
//                    if (schedules != null) {
//                        println(schedules["schedule1"])
//                    }
//                } else {
//                    print("Train data not found")
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle the error
//                // TODO: Implement error handling
//            }
//        })
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_activity, container, false)
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment SupportFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            ActivityFragment2().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//}