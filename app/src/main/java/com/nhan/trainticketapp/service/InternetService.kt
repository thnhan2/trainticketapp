package com.nhan.trainticketapp.service

import android.content.Context
import android.net.ConnectivityManager

class InternetService {
    companion object {
        private var hasInternet: Boolean = false

        fun getIsInternetAvailable(context: Context): Boolean {
            return isInternetAvailable(context)
        }

        private fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

        fun setInternetAvailability(isAvailable: Boolean) {
            hasInternet = isAvailable
        }

        fun hasInternet(): Boolean {
            return hasInternet
        }
    }
}