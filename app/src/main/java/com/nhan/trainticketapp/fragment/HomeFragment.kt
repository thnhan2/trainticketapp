package com.nhan.trainticketapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nhan.trainticketapp.R
import com.nhan.trainticketapp.model.Train

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // doc data tu db
        val firebaseTrainData = FirebaseTrainData()
        firebaseTrainData.fetchDataFromFirebase(object : FirebaseTrainData.OnDataReceivedListener {
            override fun onDataReceived(trainList: ArrayList<Train>) {
                // Ở đây bạn có thể làm gì đó với danh sách các đối tượng Train
                for (train in trainList) {
                    println("Train ID: ${train.train_id}")
                    println("Origin: ${train.origin}")
                    println("Destination: ${train.destination}")
                    println("---------------------")
                }
            }
        })
    }


class FirebaseTrainData {

    interface OnDataReceivedListener {
        fun onDataReceived(trainList: ArrayList<Train>)
    }

    fun fetchDataFromFirebase(listener: OnDataReceivedListener) {
        val databaseReference = FirebaseDatabase.getInstance("https://trainticket-19d0e-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("trains")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val trainList = ArrayList<Train>()

                for (trainSnapshot in dataSnapshot.children) {
                    val train = trainSnapshot.getValue(Train::class.java)
                    train?.let {
                        trainList.add(it)
                    }
                }

                listener.onDataReceived(trainList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Xử lý lỗi nếu có
                println("Lỗi: ${databaseError.message}")
            }
        })
    }


}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = HomeFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}