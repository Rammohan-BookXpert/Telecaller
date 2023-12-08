package com.fairsoft.telecaller.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("ConstantLocale")
val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

private val calendar = Calendar.getInstance()
val currentYear = calendar.get(Calendar.YEAR)
val currentMonth = calendar.get(Calendar.MONTH)
val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

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

fun toServerDate(date: String): String {
    val dateSplit = date.split("-")
    val calendar = Calendar.getInstance()
    calendar.set(dateSplit[2].toInt(), dateSplit[1].toInt() - 1, dateSplit[0].toInt())
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
}

fun getFormattedDate(day: Int, month: Int, year: Int): String {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, day)
    return sdf.format(calendar.time)
}