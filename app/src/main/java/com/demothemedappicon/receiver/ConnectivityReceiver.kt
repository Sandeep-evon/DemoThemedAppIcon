package com.demothemedappicon.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class ConnectivityReceiver:BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        connectivityReceiverListener?.onNetworkConnectionChanged(checkInternetConnection(context))
    }
    private fun checkInternetConnection(context: Context?):Boolean{
        val connMgr = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
    companion object{
         var connectivityReceiverListener: ConnectivityReceiverListener? = null
    }
    interface ConnectivityReceiverListener{
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }
}