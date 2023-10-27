package com.fairsoft.telecaller.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import androidx.lifecycle.LiveData

class NetworkConnectivity(
    context: Context
): LiveData<Boolean>() {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var networkConnectionCallback: ConnectivityManager.NetworkCallback

    private fun connectionCallback(): ConnectivityManager.NetworkCallback {

        networkConnectionCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                postValue(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                postValue(false)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                postValue(false)
            }
        }

        return networkConnectionCallback
    }

    override fun onActive() {
        super.onActive()
        Log.i(TAG, "Network Connectivity -> onActive is called")
        connectivityManager.registerDefaultNetworkCallback(connectionCallback())
    }
}