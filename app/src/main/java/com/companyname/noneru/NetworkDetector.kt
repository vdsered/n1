package com.companyname.noneru

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.annotation.RequiresApi
import kotlin.reflect.KFunction0

class NetworkStateReceiver: BroadcastReceiver() {
    object Global {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    }

    private val connectionListeners: MutableList<KFunction0<Unit>> = mutableListOf()

    fun addListener(connectionListener: () -> Unit) {
        connectionListeners.add(connectionListener as KFunction0<Unit>)
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        if(!isConnectionOkay(context!!))
            return

        for(listener in connectionListeners) {
            listener.invoke()
        }
    }

    private fun isConnectionOkay(context: Context): Boolean {
        return try {
            val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo != null && networkInfo.isConnected
        } catch (e: Exception) {
            false
        }

    }
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class NetworkStateCallback: ConnectivityManager.NetworkCallback() {
    private val connectionListeners: MutableList<KFunction0<Unit>> = mutableListOf()

    fun addListener(connectionListener: () -> Unit) {
        connectionListeners.add(connectionListener as KFunction0<Unit>)
    }

    override fun onAvailable(network: Network?) {
        super.onAvailable(network)
        for(listener in connectionListeners) {
            listener.invoke()
        }
    }
}

class NetworkDetector {
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkStateCallback: NetworkStateCallback
    private lateinit var networkStateReceiver: NetworkStateReceiver
    fun onCreate(activity: Activity) {
        connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            networkStateCallback = NetworkStateCallback()
            connectivityManager.registerDefaultNetworkCallback(networkStateCallback)
        }
        else {
            networkStateReceiver = NetworkStateReceiver()
            activity
                .registerReceiver(networkStateReceiver, NetworkStateReceiver.Global.intentFilter)
        }
    }

    /*
        removeListener is not implemented because we is not used in the project.
     */
    fun addListener(connectionListener: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            networkStateCallback.addListener(connectionListener)
        }
        else {
            networkStateReceiver.addListener(connectionListener)
        }
    }

    fun onDestroy(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.unregisterNetworkCallback(networkStateCallback)
        }
        else {
            activity
                .unregisterReceiver(networkStateReceiver)
        }
    }
}