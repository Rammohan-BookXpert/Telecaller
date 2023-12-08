package com.fairsoft.telecaller.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairsoft.telecaller.model.CampConNotCon
import com.fairsoft.telecaller.model.Campaign
import com.fairsoft.telecaller.model.CampaignDetailed
import com.fairsoft.telecaller.model.ContactHistory
import com.fairsoft.telecaller.model.UserSummary
import com.fairsoft.telecaller.network.NetworkApi
import com.fairsoft.telecaller.utils.TAG
import com.fairsoft.telecaller.utils.isOnline
import com.fairsoft.telecaller.utils.sdf
import com.fairsoft.telecaller.utils.showErrorToast
import com.fairsoft.telecaller.utils.showNetworkDialog
import com.lrm.bookxpert.utils.LoadingDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class AppViewModel: ViewModel() {

    var userId: Int = 0
    var companyLogged: Int = 0
    var username: String = ""

    private val _campaignsList = MutableLiveData<MutableList<Campaign>>(mutableListOf())
    val campaignsList: LiveData<MutableList<Campaign>> get() = _campaignsList

    private val _notConnectedCallsList = MutableLiveData<MutableList<CampConNotCon>>(mutableListOf())
    val notConnectedCallsList: LiveData<MutableList<CampConNotCon>> get() = _notConnectedCallsList

    private val _campaignByIdList = MutableLiveData<MutableList<CampaignDetailed>>(mutableListOf())
    val campaignByIdList: LiveData<MutableList<CampaignDetailed>> get() = _campaignByIdList

    private val _contactHistory = MutableLiveData<MutableList<ContactHistory>>(mutableListOf())
    val contactHistory: LiveData<MutableList<ContactHistory>> get() = _contactHistory

    private val _campConByIdList = MutableLiveData<MutableList<CampConNotCon>>(mutableListOf())
    val campConByIdList: LiveData<MutableList<CampConNotCon>> get() = _campConByIdList

    private val _campNotConByIdList = MutableLiveData<MutableList<CampConNotCon>>(mutableListOf())
    val campNotConByIdList: LiveData<MutableList<CampConNotCon>> get() = _campNotConByIdList

    var fromDate: String = sdf.format(Calendar.getInstance().time)
    var toDate: String = sdf.format(Calendar.getInstance().time)

    private val _userSummary = MutableLiveData(UserSummary())
    val userSummary: LiveData<UserSummary> get() = _userSummary

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

    fun getCampConByIdList(activity: Activity, campaignId: Int) {
        val loadingDialog = LoadingDialog(activity)
        loadingDialog.startLoading()

        _campConByIdList.value?.clear()
        if (isOnline(activity)) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    Log.i(TAG, "getCampConByIdList: params -> userId: $userId, campaignId: $campaignId, company: $companyLogged")
                    val response = NetworkApi.retrofitService.getConCallsById(companyLogged, campaignId, userId).toMutableList()
                    Log.i(TAG, "getCampConByIdList: -> response -> $response")
                    _campConByIdList.postValue(response)
                    loadingDialog.dismissDialog()
                } catch (e: Exception) {
                    loadingDialog.dismissDialog()
                    showErrorToast(activity)
                    e.printStackTrace()
                    Log.i(TAG, "getCampConByIdList -> Exception -> ${e.message} ")
                }
            }
        } else {
            loadingDialog.dismissDialog()
            showNetworkDialog(activity)
        }
    }

    fun getCampNotConByIdList(activity: Activity, campaignId: Int) {
        val loadingDialog = LoadingDialog(activity)
        loadingDialog.startLoading()

        _campNotConByIdList.value?.clear()
        if (isOnline(activity)) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    Log.i(TAG, "getCampNotConByIdList: params -> userId: $userId, campaignId: $campaignId, company: $companyLogged")
                    val response = NetworkApi.retrofitService.getNotConCallsById(campaignId, companyLogged, userId)
                    Log.i(TAG, "getCampNotConByIdList: -> response -> $response")
                    _campNotConByIdList.postValue(response.toMutableList())
                    loadingDialog.dismissDialog()
                } catch (e: Exception) {
                    loadingDialog.dismissDialog()
                    showErrorToast(activity)
                    e.printStackTrace()
                    Log.i(TAG, "getCampNotConByIdList -> Exception -> ${e.message} ")
                }
            }
        } else {
            loadingDialog.dismissDialog()
            showNetworkDialog(activity)
        }
    }

    fun getUserSummaryByDates(activity: Activity, fromDate: String, toDate: String) {
        val loadingDialog = LoadingDialog(activity)
        loadingDialog.startLoading()

        if (isOnline(activity)) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    Log.i(TAG, "getUserSummaryByDates: params -> userId: $userId, company: $companyLogged, fromDate: $fromDate " +
                            "toDate: $toDate")
                    val response = NetworkApi.retrofitService.getUserSummaryByDates(userId, companyLogged, fromDate, toDate)
                    Log.i(TAG, "getUserSummaryByDates: -> response -> $response")
                    loadingDialog.dismissDialog()
                    _userSummary.postValue(response)
                } catch (e: Exception) {
                    loadingDialog.dismissDialog()
                    showErrorToast(activity)
                    e.printStackTrace()
                    Log.i(TAG, "getUserSummaryByDates -> Exception -> ${e.message} ")
                }
            }
        } else {
            loadingDialog.dismissDialog()
            showNetworkDialog(activity)
        }
    }
}