package com.nhan.trainticketapp.fragment

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nhan.trainticketapp.EditProfileActivity
import com.nhan.trainticketapp.SignInActivity
import com.nhan.trainticketapp.databinding.FragmentAccountBinding
import java.util.Locale



class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.tvChangeLanguage.setOnClickListener {
            val context = LanguageUtil.changeLang(requireContext(), "vi")
            val ft = fragmentManager?.beginTransaction()
            ft?.detach(this)
            ft?.attach(this)
            ft?.commit()
        }

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        val userRef = userId?.let {
            FirebaseDatabase.getInstance("https://trainticket-19d0e-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("users").child(it)
        }
        userRef!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userData = dataSnapshot.value as? Map<*, *>
                val uri = userData?.get("image")
                val username = userData?.get("name")

                binding.tvUserName.text = username.toString()
                Glide.with(this@AccountFragment).load(uri).into(binding.circleImageView)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        binding.tvLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            // Chuyển về màn hình đăng nhập sau khi đăng xuất
            val loginIntent = Intent(requireContext(), SignInActivity::class.java)
            loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(loginIntent)
            requireActivity().finish()
        }

        return binding.root
    }

        object LanguageUtil {
        fun changeLang(context: Context, lang: String): Context {
            val locale = Locale(lang)
            Locale.setDefault(locale)

            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)

            return context.createConfigurationContext(config)
        }
    }
}