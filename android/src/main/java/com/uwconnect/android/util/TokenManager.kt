package com.uwconnect.android.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import java.util.*

data class JWT(
    val id: Int,
    val type: String,
)

object TokenManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val jwtToken = stringPreferencesKey("jwt_token")

    private lateinit var dataStore: DataStore<Preferences>
    fun init(context: Context) {
        dataStore = context.dataStore
    }

    fun parseJwt(jwt: String): JWT {
        val parts = jwt.split('.')
        val payload = JSONObject(String(Base64.getUrlDecoder().decode(parts[1])))
        return JWT(payload.getInt("id"), payload.getString("type"))
    }

    suspend fun getJwt(): String {
        val preferences = dataStore.data.first()
        return preferences[jwtToken].toString()
    }

    suspend fun saveJwt(token: String) {
        dataStore.edit { preferences ->
            preferences[jwtToken] = token
        }
    }
}

