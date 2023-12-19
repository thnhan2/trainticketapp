package com.nhan.trainticketapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            // hide action bar
        supportActionBar?.hide()


        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        Handler(Looper.getMainLooper()).postDelayed({
            val user = auth.currentUser
            if (user != null) {

                // da dang nhap -> go home
                val intent = Intent(this, HomeActivity::class.java)
                var curUsername = "New User"
                if (user.displayName != null) {
                    curUsername = user.displayName.toString()
                }
                println(uid)
                println("kA0mHkJVAzdU2K8XJsErksI9VmI2")
                val database = FirebaseDatabase.getInstance("https://trainticket-19d0e-default-rtdb.asia-southeast1.firebasedatabase.app")
                val myRef = database.getReference("/users/kA0mHkJVAzdU2K8XJsErksI9VmI2")

                myRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val username = dataSnapshot.child("name").value.toString()
                        // Lưu tên người dùng vào curUsername
                        curUsername = username
                        intent.putExtra("current_user_name", curUsername)
                        startActivity(intent)

                        finish()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                        Log.w("tag", "Failed to read value.", error.toException())
                    }
                })
            } else {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 1000)


    }



}