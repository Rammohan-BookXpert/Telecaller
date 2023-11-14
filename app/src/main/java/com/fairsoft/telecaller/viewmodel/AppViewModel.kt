package com.fairsoft.telecaller.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairsoft.telecaller.model.CampNotConnected
import com.fairsoft.telecaller.model.Campaign
import com.fairsoft.telecaller.model.CampaignDetailed
import com.fairsoft.telecaller.model.ContactHistory
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

    var calledOnce = false

    private val _campaignsList = MutableLiveData<MutableList<Campaign>>(mutableListOf())
    val campaignsList: LiveData<MutableList<Campaign>> get() = _campaignsList

    private val _notConnectedCallsList = MutableLiveData<MutableList<CampNotConnected>>(mutableListOf())
    val notConnectedCallsList: LiveData<MutableList<CampNotConnected>> get() = _notConnectedCallsList

    private val _campaignByIdList = MutableLiveData<MutableList<CampaignDetailed>>(mutableListOf())
    val campaignByIdList: LiveData<MutableList<CampaignDetailed>> get() = _campaignByIdList

    private val _contactHistory = MutableLiveData<MutableList<ContactHistory>>(mutableListOf())
    val contactHistory: LiveData<MutableList<ContactHistory>> get() = _contactHistory

    fun getCampaignsList(activity: Activity) {
        val loadingDialog = LoadingDialog(activity)
        loadingDialog.startLoading()

        _campaignsList.value?.clear()
        if (isOnline(activity)) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    Log.i(TAG, "getCampaignsList: params -> userId: $userId, company: $companyLogged")
                    val response = NetworkApi.retrofitService.getCampaignsList(userId, companyLogged).toMutableList()
                    Log.i(TAG, "getCampaignsList: -> response -> $response")
                    _campaignsList.postValue(response)
                    loadingDialog.dismissDialog()
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

        _notConnectedCallsList.value?.clear()
        if (isOnline(activity)) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    Log.i(TAG, "getCampNotConnectedCallsList: params -> company: $companyLogged, userId: $userId")
                    val response = NetworkApi.retrofitService.getNotConnectedCallsList(companyLogged, userId).toMutableList()
                    Log.i(TAG, "getCampNotConnectedCallsList: -> response -> $response")
                    _notConnectedCallsList.postValue(response)
                    loadingDialog.dismissDialog()
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

    fun getCampaignById(activity: Activity, campaignId: Int) {
        val loadingDialog = LoadingDialog(activity)
        loadingDialog.startLoading()

        _campaignByIdList.value?.clear()
        if (isOnline(activity)) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    Log.i(TAG, "getCampaignById: params -> userId: $userId, campaignId: $campaignId, company: $companyLogged")
                    val response = NetworkApi.retrofitService.getCampaignById(userId, campaignId, companyLogged).toMutableList()
                    Log.i(TAG, "getCampaignById: -> response -> $response")
                    _campaignByIdList.postValue(response)
                    loadingDialog.dismissDialog()
                } catch (e: Exception) {
                    loadingDialog.dismissDialog()
                    showErrorToast(activity)
                    e.printStackTrace()
                    Log.i(TAG, "getCampaignById -> Exception -> ${e.message} ")
                }
            }
        } else {
            loadingDialog.dismissDialog()
            showNetworkDialog(activity)
        }
    }

    fun getContactHistory(activity: Activity, mobileNum: String) {
        val loadingDialog = LoadingDialog(activity)
        loadingDialog.startLoading()

        _contactHistory.value?.clear()
        if (isOnline(activity)) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    Log.i(TAG, "getContactHistory: params -> mobileNum: $mobileNum, company: $companyLogged")
                    val response = NetworkApi.retrofitService.getContactHistory(mobileNum, companyLogged).toMutableList()
                    Log.i(TAG, "getContactHistory: -> response -> $response")
                    _contactHistory.postValue(response)
                    loadingDialog.dismissDialog()
                } catch (e: Exception) {
                    loadingDialog.dismissDialog()
                    showErrorToast(activity)
                    e.printStackTrace()
                    Log.i(TAG, "getContactHistory -> Exception -> ${e.message} ")
                }
            }
        } else {
            loadingDialog.dismissDialog()
            showNetworkDialog(activity)
        }
    }
}