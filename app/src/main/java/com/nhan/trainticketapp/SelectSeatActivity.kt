package com.nhan.trainticketapp

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nhan.trainticketapp.model.Seat


class SelectSeatActivity : AppCompatActivity() {

    private val database =
        FirebaseDatabase.getInstance("https://trainticket-19d0e-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val seatRef = database.getReference("/seat")
    private val ticketRef = database.getReference("/ticket")
    private val selectedSeats =
        mutableListOf<Int>() // Danh sách lưu trữ các vị trí ô đang được chọn
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
        val id = intent.getStringExtra("trainId")

        println("seat select train id = $id")
        val query = seatRef.orderByChild("train_id").equalTo(id!!.toDouble())

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
        val btnBook: Button = findViewById(R.id.btnBook)

        btnBook.setOnClickListener {
            val userUID =
                FirebaseAuth.getInstance().currentUser?.uid // Thay thế bằng userUID thực tế của bạn
            val trainId =
                intent.getStringExtra("trainId")?.toInt() // Thay thế bằng train_id thực tế của bạn

            seatRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var seatsAddedCount = 0 // Số lượng vé đã được thêm
                    val lastSeatIndex = selectedSeats.lastIndex

                    for (seat in selectedSeats) {
                        val seatNode = dataSnapshot.children.find { childSnapshot ->
                            val trainIdValue = childSnapshot.child("train_id").getValue(Long::class.java)
                            trainIdValue?.toInt() == trainId
                        }

                        if (seatNode != null) {
                            seatNode.child("position").child(seat.toString()).ref.setValue(true)
                            seatsAddedCount++
                        }
                    }

                    for (seat in selectedSeats) {
                        val userTicketData = hashMapOf<String, Any>(
                            "user_id" to userUID!!,
                            "seat" to seat,
                            "train_id" to trainId!!
                        )

                        val userTicketRef = ticketRef.push()
                        userTicketRef.setValue(userTicketData).addOnSuccessListener {
                            if (seatsAddedCount == selectedSeats.size && seat == selectedSeats.last()) {
                                val message = "Your purchase successful for ${selectedSeats.size} tickets"
                                showPopup(message)
                            }
                        }.addOnFailureListener {
                            val message = "Failed to book ticket for seat $seat"
                            Log.e("error", message)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Xử lý lỗi khi không thể đọc dữ liệu từ Firebase
                }
            })
        }
    }

    // popup dialog
    private fun showPopup(data: String) {
        val dialog = Dialog(this@SelectSeatActivity)
        dialog.setContentView(R.layout.popup_layout) // popupview là tên layout XML của bạn

// Lấy đối tượng View từ layout XML
        val inflater = LayoutInflater.from(this@SelectSeatActivity)
        val popupView = inflater.inflate(R.layout.popup_layout, null)
        val textView = popupView.findViewById<TextView>(R.id.textViewMessage)
        textView.text = data
// Gán view đã lấy cho Dialog
        dialog.setContentView(popupView)

// Hiển thị Dialog
        dialog.show()
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