package com.example.findpill.ui.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.findpill.data.FavoriteKeys
import com.example.findpill.data.model.PillInfo
import com.example.findpill.data.repository.GetPillById
import com.example.findpill.data.settingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
        application: Application,
    private val getPillById: GetPillById
    ) : AndroidViewModel(application) {
    private val dataStore = application.settingsDataStore

    val favorites: Flow<Set<String>> = dataStore.data
        .map {
            it[FavoriteKeys.FAVORITES] ?: emptySet()
        }

    private val _pillList = MutableStateFlow<List<PillInfo>>(emptyList())
    val pillList: StateFlow<List<PillInfo>> = _pillList

    fun loadFavoritePills(){
        viewModelScope.launch{
            favorites.collect { ids ->
                val result = coroutineScope {
                    ids.map { id ->
                        async {
                            try {
                                getPillById.getPillById(id.toInt())
                            } catch (e: Exception) {
                                null
                            }
                        }
                    }.awaitAll().filterNotNull()
                }
                _pillList.value = result
            }
        }
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

    fun clearFavorite() = viewModelScope.launch {
        dataStore.edit { prefs ->
            prefs[FavoriteKeys.FAVORITES] = emptySet()
        }
    }
}