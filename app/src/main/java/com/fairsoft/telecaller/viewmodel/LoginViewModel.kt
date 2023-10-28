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
import com.fairsoft.telecaller.datastore.LoggedUserDataStore
import com.fairsoft.telecaller.model.UserLogin
import com.fairsoft.telecaller.network.NetworkApi
import com.fairsoft.telecaller.utils.TAG
import com.fairsoft.telecaller.utils.isOnline
import com.google.gson.Gson
import com.lrm.bookxpert.utils.LoadingDialog
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginViewModel : ViewModel() {

    private val _loginStatus = MutableLiveData("Pending")
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

    fun verifyLogin(
        activity: Activity,
        context: Context,
        username: String,
        password: String,
        selectedCompany: Int
    ) {
        val loadingDialog = LoadingDialog(activity)

        if (isOnline(context)) {

            val user = UserLogin(
                isBookXpertUser = selectedCompany,
                username = username,
                password = password
            )

            Log.i(TAG, "verifyLogin: -> userData -> dataClass -> $user")

            loadingDialog.startLoading()

            viewModelScope.launch {
                try {
                    val response = NetworkApi.retrofitService.checkLogin(user)
                    Log.i(TAG, "verifyLogin: -> response -> $response")

                    if (response.toString().contains("Invalid User..!")) {
                        loadingDialog.dismissDialog()
                        Toast.makeText(context, "Invalid User", Toast.LENGTH_SHORT).show()
                    } else {
                        val obj = JSONObject(Gson().toJson(response))
                        val userId: Double = obj["UserId"] as Double
                        Log.i(TAG, "verifyLogin -> response returned -> converted to Obj -> $obj")
                        Log.i(TAG, "verifyLogin -> userId -> from Obj -> ${userId.toInt()}")

                        loadingDialog.dismissDialog()
                        val loggedDataStore = LoggedUserDataStore(context)
                        loggedDataStore.saveLoginStatus(context, true)
                        loggedDataStore.saveCompanyLogged(context, selectedCompany.toString())
                        loggedDataStore.saveUserId(context, userId.toString())
                        loggedDataStore.saveUsername(context, username)
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