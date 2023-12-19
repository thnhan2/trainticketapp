package com.nhan.trainticketapp.service

import com.google.firebase.database.*
import com.nhan.trainticketapp.model.Train

class TrainService {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://trainticket-19d0e-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val reference: DatabaseReference = database.reference.child("train")

    fun getFilteredTrainData(origin: String, destination: String, callback: (Map<String, Train>) -> Unit) {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val trainData: MutableMap<String, Train> = mutableMapOf()

                for (childSnapshot in dataSnapshot.children) {
                    val trainId = childSnapshot.key
                    val train = childSnapshot.getValue(Train::class.java)
                    train?.let {
                        if (it.origin == origin && it.destination == destination) {
                            trainData[trainId ?: ""] = it
                        }
                    }
                }

                callback(trainData)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // TODO: Xu ly loi
            }
        })
    }
    fun getTrainData(callback: (Map<String, Train>) -> Unit) {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val trainData: MutableMap<String, Train> = mutableMapOf()

                for (childSnapshot in dataSnapshot.children) {
                    val trainId = childSnapshot.key
                    val train = childSnapshot.getValue(Train::class.java)
                    train?.let { trainData[trainId ?: ""] = it }
                }

                callback(trainData)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        })
    }
}



