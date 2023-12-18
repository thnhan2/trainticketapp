package com.nhan.trainticketapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nhan.trainticketapp.databinding.ActivityHomeBinding
import com.nhan.trainticketapp.fragment.AccountFragment
import com.nhan.trainticketapp.fragment.ActivityFragment
import com.nhan.trainticketapp.fragment.HomeFragment
import com.nhan.trainticketapp.fragment.MessageFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.activity -> replaceFragment(ActivityFragment())
                R.id.message -> replaceFragment(MessageFragment())
                R.id.account -> replaceFragment(AccountFragment())
                else -> {

                }
            }
                true
        }

        // sign out demo

//        val btnSignOut: Button = findViewById(R.id.btnSignOut)
//
//        var auth: FirebaseAuth
//        btnSignOut.setOnClickListener {
//            auth = Firebase.auth
//            auth.signOut()
//
//            val intent = Intent(this, SignInActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
//            finish()
//        }

    }
    private fun replaceFragment(fragment : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}