package com.example.taofamily.features.initiation.presentation.syncScreen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.taofamily.core.utils.UiState
import com.example.taofamily.features.initiation.data.repository.InitiationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SyncViewModel(
    private val initiationRepository: InitiationRepository,
): ScreenModel {


    private val _isFirstTimeSyncComplete = MutableStateFlow<UiState<Boolean>>(UiState.Ideal)
    val isFirstTimeSyncComplete = _isFirstTimeSyncComplete.asStateFlow()


//
//    init {
//        startSyncProcess()
//    }

    fun startSyncProcess(){
        screenModelScope.launch {

            try {
                _isFirstTimeSyncComplete.value = UiState.Loading

                initiationRepository.syncInitialData()
                _isFirstTimeSyncComplete.value = UiState.Success(true)
//                _isFirstTimeSyncComplete.value = UiState.Error("Sync Failed")


                println("===sync complete")
            }catch (e: Exception){
                println("===syncFailed: ${e.message}")
                _isFirstTimeSyncComplete.value = UiState.Error(e.message?:"Sync Failed")
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
        }
    }

    fun resetState(){
        _isFirstTimeSyncComplete.value = UiState.Ideal

    }
}