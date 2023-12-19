package com.nhan.trainticketapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.MultiAutoCompleteTextView

class SelectStartPointActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_start_point)

        val multiAutoCompleteTextView: MultiAutoCompleteTextView = findViewById(R.id.edtStartPoint)

        val suggestions = arrayOf("Ha Noi", "Ho Chi Minh City", "Da Nang", "Hai Phong", "Da Lat", "Sapa", "Can Tho")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, suggestions)
        multiAutoCompleteTextView.setAdapter(adapter)

        multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        multiAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedValue = adapter.getItem(position)
            val intent = Intent()
            intent.putExtra("selectedValue", selectedValue)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        val buttonBack: ImageButton = findViewById(R.id.btnBack)
        buttonBack.setOnClickListener {
            finish()
        }
    }
}
