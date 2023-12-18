package com.nhan.trainticketapp.dao

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nhan.trainticketapp.model.UserModel

class UserUtils {
    private val user = FirebaseAuth.getInstance().currentUser
    private val userId = user?.uid

    private val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("users")

    public fun getUserId(): String? {
        // always different null
        return userId
    }
    private lateinit var userModel: UserModel
    fun createUser(user: UserModel): Boolean {
        val userModel = user // tao user moi
        if (userId != null) {
            myRef.child(userId).setValue(userModel)
        }
        return true
    }

    fun updateUser(updatedUserInfo: UserModel): Boolean {
        if (userId != null) {
            myRef.child(userId).updateChildren(updatedUserInfo.toMap())
            return true
        }
        return false
    }

    companion object {
        fun getUserId(): String {
return getUserId()
        }
    }
}