package com.debugdesk.bot.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStore(private val dataStore: DataStore<Preferences>) {
    fun getBoolean(
        key: Preferences.Key<Boolean>,
        defaultValue: Boolean = false,
    ): Flow<Boolean> {
        return dataStore.data.map { preferences -> preferences[key] ?: defaultValue }
    }

    fun getString(
        key: Preferences.Key<String>,
        defaultValue: String = "",
    ): Flow<String> {
        return dataStore.data.map { preferences -> preferences[key] ?: defaultValue }
    }

    fun getInt(key: Preferences.Key<Int>): Flow<Int> {
        return dataStore.data.map { preferences -> preferences[key] ?: 0 }
    }

    suspend fun <T> set(
        key: Preferences.Key<T>,
        value: T,
    ) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun <T> remove(key: Preferences.Key<T>) {
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }

    fun <T> containsKey(key: Preferences.Key<T>): Flow<Boolean> {
        return dataStore.data.map { preference ->
            preference.contains(key)
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}