package com.example.findpill.ui.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.findpill.data.CalendarKeys
import com.example.findpill.data.settingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CalendarViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = application.settingsDataStore

    val morning: Flow<Set<String>> = dataStore.data.map {it[CalendarKeys.MORNING] ?: emptySet()}
    val afternoon: Flow<Set<String>> = dataStore.data.map {it[CalendarKeys.AFTERNOON] ?: emptySet()}
    val night: Flow<Set<String>> = dataStore.data.map {it[CalendarKeys.NIGHT] ?: emptySet()}

    fun addPlan(time:String, id:String) = viewModelScope.launch(){
        val key = when (time){
            "morning" -> CalendarKeys.MORNING
            "afternoon" -> CalendarKeys.AFTERNOON
            "night" -> CalendarKeys.NIGHT
            else -> return@launch
        }

        dataStore.edit { prefs ->
            val current = prefs[key] ?: emptySet()
            prefs[key] = current + id
        }
    }

    fun removePlan(time:String, id:String) = viewModelScope.launch(){
        val key = when(time){
            "morning" -> CalendarKeys.MORNING
            "afternoon" -> CalendarKeys.AFTERNOON
            "night" -> CalendarKeys.NIGHT
            else -> return@launch
        }

        dataStore.edit { prefs ->
            val current = prefs[key] ?: emptySet()
            prefs[key] = current - id
        }
    }
}