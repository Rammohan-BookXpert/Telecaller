package com.fairsoft.telecaller.viewmodel

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairsoft.telecaller.datastore.AppDataStore
import com.fairsoft.telecaller.network.NetworkApi
import com.fairsoft.telecaller.utils.TAG
import com.fairsoft.telecaller.utils.isOnline
import com.fairsoft.telecaller.utils.showErrorToast
import com.fairsoft.telecaller.utils.showToastMessage
import com.google.gson.Gson
import com.lrm.bookxpert.utils.LoadingDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginViewModel : ViewModel() {

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

    fun verifyLogin(activity: Activity, username: String, password: String, selectedCompany: Int) {
        val loadingDialog = LoadingDialog(activity)
        loadingDialog.startLoading()

        if (isOnline(activity)) {

            val user = mapOf("IsBookXpertUser" to "$selectedCompany",
                "UserId" to "", "UserName" to username,
                "IsLead" to "", "Password" to password,
                "Role" to "", "State" to "", "Phone" to "",
                "ISActive" to "true", "DeviceId" to "")

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val response = NetworkApi.retrofitService.checkLogin(user)
                    Log.i(TAG, "verifyLogin: -> response -> $response")
                    if (response.toString().contains("Invalid User..!")) {
                        loadingDialog.dismissDialog()
                        showToastMessage(activity, "Invalid User")
                    } else {
                        loadingDialog.dismissDialog()
                        val obj = JSONObject(Gson().toJson(response))
                        val userId: Int = (obj["UserId"] as Double).toInt()
                        val loggedDataStore = AppDataStore(activity)
                        viewModelScope.launch {
                            loggedDataStore.saveLoginStatus(activity, true)
                            loggedDataStore.saveCompanyLogged(activity, selectedCompany.toString())
                            loggedDataStore.saveUserId(activity, userId.toString())
                            loggedDataStore.saveUsername(activity, obj["UserName"].toString())
                        }
                        showToastMessage(activity, "Login Success...")
                    }
                } catch (e: Exception) {
                    loadingDialog.dismissDialog()
                    showErrorToast(activity)
                    e.printStackTrace()
                    Log.i(TAG, "verifyLogin -> Exception -> ${e.message} ")
                }
            }
        }
    }
}