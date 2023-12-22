package com.nhan.trainticketapp

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.nhan.trainticketapp.model.Train

class DetailTicketActivity : AppCompatActivity() {
    private lateinit var seat: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_ticket)
        seat = intent.getStringExtra("seat").toString()

        val qrCodeImageView = findViewById<ImageView>(R.id.qrCodeImageView)


        val text = "Your text here"  // Thay đổi chuỗi này thành văn bản bạn muốn mã hóa
        val width = 500
        val height = 500

        val bitmap = generateQRCode(text, width, height)
        qrCodeImageView.setImageBitmap(bitmap)
//        initView()
        val trainId = intent.getStringExtra("trainId").toString()  // Thay thế bằng train_id thực tế
        fetchTrainData(trainId)


    }


    private fun initView() {

    }


    private fun generateQRCode(text: String, width: Int, height: Int): Bitmap {
        val result: BitMatrix
        try {
            result = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, null)
        } catch (e: IllegalArgumentException) {
            // Unsupported format
            return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        } catch (e: WriterException) {
            return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }

        val w = result.width
        val h = result.height
        val pixels = IntArray(w * h)
        for (y in 0 until h) {
            val offset = y * w
            for (x in 0 until w) {
                pixels[offset + x] = if (result.get(x, y)) -0x1000000 else -0x1
            }
        }

        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, w, h)
        return bitmap
    }

    private fun fetchTrainData(trainId: String) {
        val database =
            FirebaseDatabase.getInstance("https://trainticket-19d0e-default-rtdb.asia-southeast1.firebasedatabase.app")
        val trainRef = database.getReference("train")

        trainRef.orderByChild("train_id").equalTo(trainId.toDouble())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (childSnapshot in dataSnapshot.children) {
                        val train = childSnapshot.getValue(Train::class.java)
                        if (train != null) {
                            runOnUiThread {
                                val tv = findViewById<TextView>(R.id.tvTitle)
                                val tvDestination = findViewById<TextView>(R.id.tvDestination)
                                val tvOrigin = findViewById<TextView>(R.id.tvOrigin)
                                val tvDateStart = findViewById<TextView>(R.id.tvDateStart)
                                val tvTimeStart = findViewById<TextView>(R.id.tvTimeStart)
                                val tvDescription = findViewById<TextView>(R.id.tvDescription)
                                val tvSeat = findViewById<TextView>(R.id.tvSeat)
                                val tvTrainId = findViewById<TextView>(R.id.tvTrainID)
                                tvDestination.text = train.destination
                                tvOrigin.text = train.origin
                                tvDateStart.text = train.schedules[0].departure_date
                                tvTimeStart.text = train.schedules[0].departure_time
                                tvDescription.text = train.description
                                tvSeat.text = seat
                                tvTrainId.text = trainId

                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("Error: ${databaseError.message}")
                }
            })
    }


}
