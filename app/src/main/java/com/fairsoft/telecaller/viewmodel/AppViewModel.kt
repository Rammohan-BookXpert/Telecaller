package com.fairsoft.telecaller.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairsoft.telecaller.model.CampNotConnected
import com.fairsoft.telecaller.model.Campaign
import com.fairsoft.telecaller.network.NetworkApi
import com.fairsoft.telecaller.utils.TAG
import com.fairsoft.telecaller.utils.isOnline
import com.fairsoft.telecaller.utils.showErrorToast
import com.fairsoft.telecaller.utils.showNetworkDialog
import com.lrm.bookxpert.utils.LoadingDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel: ViewModel() {

    var userId: Int = 0
    var companyLogged: Int = 0
    var username: String = ""

    private val _campaignsList = MutableLiveData<MutableList<Campaign>>(mutableListOf())
    val campaignsList: LiveData<MutableList<Campaign>> get() = _campaignsList

    private val _notConnectedCallsList = MutableLiveData<MutableList<CampNotConnected>>(mutableListOf())
    val notConnectedCallsList: LiveData<MutableList<CampNotConnected>> get() = _notConnectedCallsList

    fun getCampaignsList(activity: Activity) {
        val loadingDialog = LoadingDialog(activity)
        loadingDialog.startLoading()

        if (isOnline(activity)) {
            _campaignsList.value?.clear()
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    loadingDialog.dismissDialog()
                    Log.i(TAG, "getCampaignsList: params -> userId: $userId, company: $companyLogged")
                    val response = NetworkApi.retrofitService.getCampaignsList(userId, companyLogged).toMutableList()
                    Log.i(TAG, "getCampaignsList: -> response -> $response")
                    _campaignsList.postValue(response)
                } catch (e: Exception) {
                    loadingDialog.dismissDialog()
                    showErrorToast(activity)
                    e.printStackTrace()
                    Log.i(TAG, "getCampaignsList -> Exception -> ${e.message} ")
                }
            }
        } else {
            loadingDialog.dismissDialog()
            showNetworkDialog(activity)
        }
    }

    fun getCampNotConnectedCallsList(activity: Activity) {
        val loadingDialog = LoadingDialog(activity)
        loadingDialog.startLoading()

        if (isOnline(activity)) {
            _notConnectedCallsList.value?.clear()
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    loadingDialog.dismissDialog()
                    Log.i(TAG, "getCampNotConnectedCallsList: params -> company: $companyLogged, userId: $userId")
                    val response = NetworkApi.retrofitService.getNotConnectedCallsList(companyLogged, userId).toMutableList()
                    Log.i(TAG, "getCampNotConnectedCallsList: -> response -> $response")
                    _notConnectedCallsList.postValue(response)
                } catch (e: Exception) {
                    loadingDialog.dismissDialog()
                    showErrorToast(activity)
                    e.printStackTrace()
                    Log.i(TAG, "getCampNotConnectedCallsList -> Exception -> ${e.message} ")
                }
            }
        } else {
            loadingDialog.dismissDialog()
            showNetworkDialog(activity)
        }
    }
}