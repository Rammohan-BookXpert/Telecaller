package com.fairsoft.telecaller.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

private const val LOGGED_USER_PREFERENCE = "logged_user_preference"

val Context.dataStore
        : DataStore<Preferences> by preferencesDataStore(name = LOGGED_USER_PREFERENCE)

class LoggedUserDataStore(context: Context) {
    private val USER_ID = stringPreferencesKey("user_id")
    private val USER_NAME = stringPreferencesKey("username")
    private val IS_BOOK_XPERT = stringPreferencesKey("is_book_xpert")
}