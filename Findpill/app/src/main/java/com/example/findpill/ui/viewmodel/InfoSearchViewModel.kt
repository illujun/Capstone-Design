package com.example.findpill.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findpill.data.model.InfoSearchRequest
import com.example.findpill.data.model.PillInfo
import com.example.findpill.data.repository.InfoSearchRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoSearchViewModel @Inject constructor(
    private val repository: InfoSearchRepo
) : ViewModel() {
    private val _result = MutableStateFlow<List<PillInfo>>(emptyList())
    val result: StateFlow<List<PillInfo>> = _result

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun search(request: InfoSearchRequest, onSuccess: () -> Unit){
        viewModelScope.launch{
            _loading.value = true
            _error.value = null

            val response = repository.pillInfoSearch(request)
            if(response != null){
                _result.value = response
                onSuccess()
            }else{
                _error.value = "검색 실패"
                Log.w("InfoSearchViewModel", "API 응답이 null입니다. 서버 응답 확인 필요.")
            }

            _loading.value = false
        }
    }
}