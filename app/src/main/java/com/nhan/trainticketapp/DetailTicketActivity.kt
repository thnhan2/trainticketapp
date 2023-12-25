package com.nhan.trainticketapp

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.toBitmap
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.nhan.trainticketapp.model.Train
import java.io.OutputStream

class DetailTicketActivity : AppCompatActivity() {
    private lateinit var seat: String
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_ticket)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
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

        val btnDownload=findViewById<Button>(R.id.btnDownload)
        btnDownload.setOnClickListener {
            saveTicketImageToGallery()
        }

        val btnShare = findViewById<ImageButton>(R.id.btnShare)
        btnShare.setOnClickListener {
            shareTicket()
        }

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
                                val tvStartPoint = findViewById<TextView>(R.id.tvStartPoint)
                                val tvDestination = findViewById<TextView>(R.id.tvDestination)
                                val tvDuration = findViewById<TextView>(R.id.tvDuration)
                                val tvDateStart = findViewById<TextView>(R.id.tvDateStart)
                                val tvTimeStart = findViewById<TextView>(R.id.tvTimeStart)
                                val tvSeat = findViewById<TextView>(R.id.tvSeat)
                                val tvTrainId = findViewById<TextView>(R.id.tvTrainID)

                                tvStartPoint.append(" "+train.origin)
                                tvDuration.append(" "+convertMinutesToHoursAndMinutes(train.duration).toString())
                                tvDestination.append(" " +train.destination)
                                tvDateStart.append(" "+ train.schedules[0].departure_date)
                                tvTimeStart.append(" "+train.schedules[0].departure_time)
                                tvSeat.append(" $seat")
                                tvTrainId.append(" " +train.train_id)

                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("Error: ${databaseError.message}")
                }
            })
    }
    private fun convertMinutesToHoursAndMinutes(minutes: Int): Pair<Int, Int> {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return Pair(hours, remainingMinutes)
    }

    private fun saveTicketImageToGallery() {
        val qrCodeImageView = findViewById<ImageView>(R.id.qrCodeImageView)
        val bitmap = (qrCodeImageView.drawable).toBitmap()

        val filename = "${System.currentTimeMillis()}.png"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val contentResolver = contentResolver
        val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            val outputStream: OutputStream? = imageUri?.let {
                contentResolver.openOutputStream(it)
            }
            outputStream?.use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }

            showToast("Ticket downloaded successfully!")
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("Failed to download ticket.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@DetailTicketActivity,"download successfully",Toast.LENGTH_SHORT).show()
    }


    // share
    private fun shareTicket() {
        val qrCodeImageView = findViewById<ImageView>(R.id.qrCodeImageView)
        val bitmap = (qrCodeImageView.drawable).toBitmap()

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, getImageUri(bitmap))
            putExtra(Intent.EXTRA_TEXT, "Check out my train ticket!")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun getImageUri(bitmap: Bitmap): android.net.Uri? {
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Ticket", null)
        return android.net.Uri.parse(path)
    }

}
