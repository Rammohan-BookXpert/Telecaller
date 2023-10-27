package com.fairsoft.telecaller.viewmodel

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairsoft.telecaller.network.NetworkApi
import com.fairsoft.telecaller.utils.TAG
import com.fairsoft.telecaller.utils.isOnline
import com.google.gson.Gson
import com.lrm.bookxpert.utils.LoadingDialog
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginStatus = MutableLiveData<String>("Pending")
    val loginStatus: LiveData<String> get() = _loginStatus

    fun isLoginValid(context: Context, username: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(username) -> {
                Toast.makeText(context, "Please enter username", Toast.LENGTH_SHORT).show()
                false
            }

            TextUtils.isEmpty(password) -> {
                Toast.makeText(context, "Please enter password", Toast.LENGTH_SHORT).show()
                false
            }

            else -> true
        }
    }

    fun verifyLogin(activity: Activity, context: Context, username: String, password: String, selectedCompany: Int) {
        val loadingDialog = LoadingDialog(activity)

        if (isOnline(context)) {
            val userData = HashMap<String, String>()
            userData["IsBookXpertUser"] = selectedCompany.toString()
            userData["UserId"] = ""
            userData["UserName"] = username
            userData["IsLead"] = ""
            userData["Password"] = password
            userData["Role"] = ""
            userData["State"] = ""
            userData["Phone"] = ""
            userData["ISActive"] = "true"
            userData["DeviceId"] = ""

            Log.i(TAG, "verifyLogin: -> userData -> HashMap -> $userData")

            loadingDialog.startLoading()

            viewModelScope.launch {
                try {
                    val response = NetworkApi.retrofitService.checkLogin(userData)
                    Log.i(TAG, "verifyLogin: -> response -> $response")

                    if (response.toString().contains("Invalid User..!")) {
                        loadingDialog.dismissDialog()
                        Toast.makeText(context, "Invalid User", Toast.LENGTH_SHORT).show()
                    } else {
                        loadingDialog.dismissDialog()
                        val data = Gson().toJson(response)
                        Log.i(TAG, "verifyLogin -> response returned -> $data")
                        _loginStatus.postValue("Success")
                        Toast.makeText(context, "Login Success...", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {
                    loadingDialog.dismissDialog()
                    Toast.makeText(context, "An error occurred...", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                    Log.i(TAG, "verifyLogin -> Exception -> ${e.message} ")
                }
            }
        }
    }
}