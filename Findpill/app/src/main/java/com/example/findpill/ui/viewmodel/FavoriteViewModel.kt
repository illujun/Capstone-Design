package com.example.findpill.ui.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.findpill.data.FavoriteKeys
import com.example.findpill.data.settingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = application.settingsDataStore

    val favorites: Flow<Set<String>> = dataStore.data
        .map {
            it[FavoriteKeys.FAVORITES] ?: emptySet()
        }

    fun addFavorite(id: String) = viewModelScope.launch{
        dataStore.edit { prefs ->
            val current = prefs[FavoriteKeys.FAVORITES] ?: emptySet()
            prefs[FavoriteKeys.FAVORITES] = current + id
        }
    }

    fun removeFavorite(id:String) = viewModelScope.launch{
        dataStore.edit { prefs ->
            val current = prefs[FavoriteKeys.FAVORITES] ?: emptySet()
            prefs[FavoriteKeys.FAVORITES] = current - id
        }
    }
}