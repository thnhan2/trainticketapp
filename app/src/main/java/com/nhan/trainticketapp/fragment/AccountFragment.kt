package com.nhan.trainticketapp.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.core.app.ActivityOptionsCompat
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
private val EDIT_PROFILE_REQUEST_CODE = 1
    private var isDataRefreshed = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        binding.btnChangeDarkMode.setOnClickListener {
            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val newNightMode = if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }

            AppCompatDelegate.setDefaultNightMode(newNightMode)
            requireActivity().recreate()
        }

        binding.constraintLayout.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity())
            startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE, options.toBundle())
        }

            binding.tvSupport.setOnClickListener {
                val phoneNumber = "0946014030"
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phoneNumber")
                startActivity(intent)
        }
        val text = "Edit"
        val spannableString = SpannableString(text)
        spannableString.setSpan(UnderlineSpan(), 0, text.length, 0)
        binding.tvEdit.text = spannableString

        binding.tvReward.setOnClickListener {
            val context = LanguageUtil.changeLang(requireContext(), "vi")
            val ft = fragmentManager?.beginTransaction()
            ft?.detach(this)
            ft?.attach(this)
            ft?.commit()
        }

       refresh()

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
    private fun refresh() {
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
                context?.let { Glide.with(it).load(uri).into(binding.circleImageView) }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            isDataRefreshed = true
        }
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
    override fun onResume() {
        super.onResume()
            // Làm mới dữ liệu và hiển thị lại trang
        if (isDataRefreshed) {
            refresh()
            isDataRefreshed = false
        }
    }

}