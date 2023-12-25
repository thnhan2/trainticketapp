package com.nhan.trainticketapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nhan.trainticketapp.databinding.ActivityHomeBinding
import com.nhan.trainticketapp.fragment.AccountFragment
import com.nhan.trainticketapp.fragment.ActivityFragment
import com.nhan.trainticketapp.fragment.HomeFragment
import com.nhan.trainticketapp.fragment.MessageFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUsername = intent.getStringExtra("current_user_name")

        // Load the initial fragment
        replaceFragment(HomeFragment.newInstance(currentUsername))

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(HomeFragment.newInstance(currentUsername))
                R.id.activity -> replaceFragment(ActivityFragment())
                R.id.message -> replaceFragment(MessageFragment())
                R.id.account -> replaceFragment(AccountFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment == currentFragment) {
            // Same fragment, no need to replace
            return
        }

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()

        currentFragment = fragment  // Update the current fragment reference
    }
}
