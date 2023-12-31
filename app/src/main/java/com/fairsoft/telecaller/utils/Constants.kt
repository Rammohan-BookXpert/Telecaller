package com.fairsoft.telecaller.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

const val TAG = "MyLogMessages"

object LoggedUser {
    val USER_ID = intPreferencesKey("user_id")
    val USER_NAME = stringPreferencesKey("username")
    val IS_BXP_USER = intPreferencesKey("is_book_xpert")
    val IS_USER_LOGGED = booleanPreferencesKey("is_user_logged")
}

object PermissionCodes {
    const val CALL_PERMISSION_CODE = 100
}