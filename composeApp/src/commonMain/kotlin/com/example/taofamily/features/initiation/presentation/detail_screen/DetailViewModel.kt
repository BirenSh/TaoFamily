package com.example.taofamily.features.initiation.presentation.detail_screen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.taofamily.core.utils.UiState
import com.example.taofamily.features.initiation.data.repository.InitiationRepository
import com.example.taofamily.features.initiation.domain.model.InitiationFormFiled
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val initiateRepository: InitiationRepository
): ScreenModel {
    private val _memberDetails = MutableStateFlow<InitiationFormFiled?>(null)
    val memberDetail: StateFlow<InitiationFormFiled?> = _memberDetails.asStateFlow()

    fun getDetail(memberId: String){
        screenModelScope.launch {
            val result = initiateRepository.getEntryById(memberId)
            if (result != null){
                _memberDetails.value = result
            }else {
                _memberDetails.value = null
            }
        }
    }
}