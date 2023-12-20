package com.nhan.trainticketapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nhan.trainticketapp.model.Seat

class SelectSeatActivity : AppCompatActivity() {

    private val database =
        FirebaseDatabase.getInstance("https://trainticket-19d0e-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val seatRef = database.getReference("/seat")
    private val selectedSeats = mutableListOf<Int>() // Danh sách lưu trữ các vị trí ô đang được chọn
    private lateinit var tvAmount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_seat)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        tvAmount = findViewById(R.id.tvAmount) // Khởi tạo tvAmount

        val query = seatRef.orderByChild("train_id").equalTo(1.0)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                runOnUiThread {
                    for (childSnapshot in dataSnapshot.children) {
                        val seat = childSnapshot.getValue(Seat::class.java)
                        seat?.position?.let { positions ->
                            updateSeatViews(positions)
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                print("ko co data")
            }
        })
        val btnBook: Button= findViewById(R.id.btnBook)
        btnBook.setOnClickListener {
            Toast.makeText(this, selectedSeats.toString(), Toast.LENGTH_SHORT).show()
        }
        // purchase


    }

    private fun updateAmount(amount: String) {
        runOnUiThread {
            tvAmount.text = amount
        }
    }

    private fun updateSeatViews(positionList: List<Boolean>) {
        val seats = mutableListOf<Button>()

        for (i in 1..20) {
            val seat = findViewById<Button>(resources.getIdentifier("seat$i", "id", packageName))
            seat.setBackgroundColor(Color.GRAY)
            if (positionList[i - 1]) {
                seat.isClickable = false
                seat.setBackgroundColor(Color.parseColor("#FFC47E"))
            }

            seats.add(seat)
        }

        // Xử lý sự kiện nhấp
        seats.forEachIndexed { index, seat ->
            seat.setOnClickListener {
                if (positionList[index]) {
                    // Ô đang bị chặn, không thể chọn
                    return@setOnClickListener
                }

                // Kiểm tra nếu ô đã được chọn trước đó
                if (selectedSeats.contains(index)) {
                    selectedSeats.remove(index)
                    val ticket = selectedSeats.size
                    val value = "Amount: $ticket ticket, ${(100 * ticket)}$"
                    updateAmount(value)
                    seat.setBackgroundColor(Color.GRAY)
                } else {
                    selectedSeats.add(index)
                    val ticket = selectedSeats.size
                    val value = "Amount: $ticket ticket, ${(100 * ticket)}$"
                    updateAmount(value)
                    seat.setBackgroundColor(Color.GREEN)
                }
            }
        }
    }
}