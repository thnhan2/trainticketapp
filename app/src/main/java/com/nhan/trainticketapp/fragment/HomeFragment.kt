package com.nhan.trainticketapp.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.nhan.trainticketapp.ListTrainActivity
import com.nhan.trainticketapp.SelectStartPointActivity
import com.nhan.trainticketapp.SelectWhereToActivity
import com.nhan.trainticketapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // select start
        val tvStartPoint: TextView = binding.tvStartPoint
        tvStartPoint.setOnClickListener {
            val intent = Intent(requireContext(), SelectStartPointActivity::class.java)
            startActivityForResult(intent, REQUEST_SELECT_START_POINT)
        }

        // select destination
        val tvWhereTo: TextView = binding.tvWhereTo
        tvWhereTo.setOnClickListener {
            val intent = Intent(requireContext(), SelectWhereToActivity::class.java)
            startActivityForResult(intent, REQUEST_SELECT_WHERE_TO)
        }

        val btnReverse: ImageButton = binding.btnReverse
        btnReverse.setOnClickListener {
            val temp = binding.tvStartPoint.text
            binding.tvStartPoint.text = binding.tvWhereTo.text
            binding.tvWhereTo.text = temp
        }


        val currentUsername = "Hi " + arguments?.getString("current_user_name")
        binding.textView.text = currentUsername


        binding.btnSearch.setOnClickListener {
            val intent = Intent(requireContext(), ListTrainActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_START_POINT && resultCode == Activity.RESULT_OK) {
            val selectedValue = data?.getStringExtra("selectedValue")
            binding.tvStartPoint.text = selectedValue
        } else if (requestCode == REQUEST_SELECT_WHERE_TO && resultCode == Activity.RESULT_OK) {
            val selectedValue = data?.getStringExtra("selectedValue")
            binding.tvWhereTo.text = selectedValue
        }


    }

    companion object {
        fun newInstance(currentUsername: String?): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString("current_user_name", currentUsername)
            fragment.arguments = args
            return fragment
        }
        private const val REQUEST_SELECT_START_POINT = 1
        private const val REQUEST_SELECT_WHERE_TO = 2
    }
}
