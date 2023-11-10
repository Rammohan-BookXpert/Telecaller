package com.fairsoft.telecaller.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.fairsoft.telecaller.R

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

    if (capabilities != null) {
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
    return false
}

fun hideSoftKeyboard(activity: Activity) {
    val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
}

fun showNetworkDialog(context: Context) {
    val networkDialog = Dialog(context)
    networkDialog.apply {
        setCancelable(true)
        setContentView(R.layout.custom_network_dialog)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        findViewById<TextView>(R.id.go_settings_tv).setOnClickListener {
            val intent = Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS)
            context.startActivity(intent)
        }
        show()
    }
    Toast.makeText(context, "Please connect to internet...", Toast.LENGTH_SHORT).show()
}

fun showRegisterDeviceDialog(activity: Activity, deviceId: String) {
    val dialogView = activity.layoutInflater.inflate(R.layout.custom_register_device_id_dialog, null)
    val deviceIdTv = dialogView.findViewById<TextView>(R.id.device_id)
    val close = dialogView.findViewById<TextView>(R.id.close_tv)
    val copy = dialogView.findViewById<TextView>(R.id.copy_tv)

    val builder = AlertDialog.Builder(activity)
    builder.setView(dialogView)
    builder.setCancelable(false)

    val deviceIdDialog = builder.create()
    deviceIdDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    deviceIdDialog.show()

    deviceIdTv.text = deviceId

    close.setOnClickListener {
        deviceIdDialog.dismiss()
        activity.finish()
    }

    copy.setOnClickListener {
        val clipBoard = activity.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("deviceId", deviceId)
        clipBoard.setPrimaryClip(clip)

        activity.runOnUiThread { Toast.makeText(activity, "Copied $deviceId", Toast.LENGTH_SHORT).show() }
        activity.finish()
    }
}

fun showToastMessage(activity: Activity, message: String) {
    activity.runOnUiThread { Toast.makeText(activity, message, Toast.LENGTH_SHORT).show() }
}

fun showErrorToast(activity: Activity) {
    activity.runOnUiThread { Toast.makeText(activity, "An error occurred...", Toast.LENGTH_SHORT).show() }
}

fun showServerErrorDialog(activity: Activity) {
    activity.runOnUiThread {
        val serverErrDialog = Dialog(activity)
        serverErrDialog.apply {
            setCancelable(true)
            setContentView(R.layout.custom_server_error_dialog)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
        Toast.makeText(activity, "An error occurred...", Toast.LENGTH_SHORT).show()
    }
}