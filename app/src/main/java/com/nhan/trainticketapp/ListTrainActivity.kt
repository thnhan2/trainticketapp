package com.nhan.trainticketapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nhan.trainticketapp.adapter.TrainAdapter
import com.nhan.trainticketapp.model.Train
import com.nhan.trainticketapp.service.TrainService

class ListTrainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var trainAdapter: TrainAdapter
    private lateinit var trainList: MutableList<Train>
    private lateinit var toolbar: Toolbar

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_train)

        // toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title ="Train match your search"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        trainList = mutableListOf()

        // select seat
        trainAdapter = TrainAdapter(trainList) {
            train ->
                val intent = Intent(this,SelectSeatActivity::class.java)
            intent.putExtra("trainId", train.train_id.toString())
            println(train.train_id)
            startActivity(intent)
        }
        recyclerView.adapter = trainAdapter

        val firebaseService = TrainService()
        val start: String? = intent.getStringExtra("start")
        val end: String? = intent.getStringExtra("end")
        firebaseService.getFilteredTrainData(start!!, end!!) { filteredTrainData ->
            trainList.clear()
            trainList.addAll(filteredTrainData.values)
            trainAdapter.notifyDataSetChanged()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Xử lý sự kiện khi nhấn nút back
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}