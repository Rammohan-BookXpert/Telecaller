package com.fairsoft.telecaller.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.fairsoft.telecaller.utils.LoggedUser.IS_BXP_USER
import com.fairsoft.telecaller.utils.LoggedUser.IS_USER_LOGGED
import com.fairsoft.telecaller.utils.LoggedUser.USER_ID
import com.fairsoft.telecaller.utils.LoggedUser.USER_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val LOGGED_USER_PREFERENCE = "logged_user_preference"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = LOGGED_USER_PREFERENCE)

class LoggedUserDataStore(context: Context) {

    suspend fun saveUserId(context: Context, userId: String) {
        context.dataStore.edit {preferences ->
            preferences[USER_ID] = userId
        }
    }

    suspend fun saveUsername(context: Context, username: String) {
        context.dataStore.edit {preferences ->
            preferences[USER_NAME] = username
        }
    }

    suspend fun saveCompanyLogged(context: Context, isBookXpert: String) {
        context.dataStore.edit {preferences ->
            preferences[IS_BXP_USER] = isBookXpert
        }
    }

    suspend fun saveLoginStatus(context: Context, isUserLogged: Boolean) {
        context.dataStore.edit {preferences ->
            preferences[IS_USER_LOGGED] = isUserLogged
        }
    }

    val isUserLogged: Flow<Boolean> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[IS_USER_LOGGED] ?: false
        }
}