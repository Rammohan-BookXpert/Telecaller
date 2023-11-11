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

private const val APP_PREFERENCE = "app_preference"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = APP_PREFERENCE)

class AppDataStore(context: Context) {

    suspend fun saveLoginStatus(context: Context, isUserLogged: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_USER_LOGGED] = isUserLogged
        }
    }

    suspend fun saveUserId(context: Context, userId: Int) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    suspend fun saveUsername(context: Context, username: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = username
        }
    }

    suspend fun saveCompanyLogged(context: Context, isBookXpert: Int) {
        context.dataStore.edit { preferences ->
            preferences[IS_BXP_USER] = isBookXpert
        }
    }

    val loginStatus: Flow<Boolean> = context.dataStore.data
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

    val userId: Flow<Int> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[USER_ID] ?: 0
        }

    val username: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[USER_NAME] ?: ""
        }

    val companyLogged: Flow<Int> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[IS_BXP_USER] ?: 0
        }

    suspend fun clearAllPreferences(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}