package com.example.taofamily.features.initiation.presentation.taochin_screen

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.taofamily.core.utils.UiState
import com.example.taofamily.features.initiation.data.repository.InitiationRepository
import com.example.taofamily.features.initiation.data.repository.InitiationRepositoryImpl
import com.example.taofamily.features.initiation.domain.model.Gender
import com.example.taofamily.features.initiation.domain.model.InitiationFormFiled
import com.example.taofamily.features.initiation.domain.model.Temple
import io.ktor.util.Hash.combine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.emptyList

class MemberListViewModel(
    private val initiationRepository: InitiationRepository
) : ScreenModel{


    private val allEntries : Flow<List<InitiationFormFiled>> = initiationRepository.getAllEntries()
    private val _state = MutableStateFlow<UiState<List<InitiationFormFiled>>>(UiState.Ideal )
    val state: StateFlow<UiState<List<InitiationFormFiled>>> = _state

    private val _searchQuery =  MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    init {
        listenToFilteredEntries()
    }

    private fun listenToFilteredEntries() {
        screenModelScope.launch {
            combine(allEntries,_searchQuery,_filterState){entries,query,filter->

                val filteredList = entries.filter { entry ->
                    val q = query.trim().lowercase()

                    // A. Search Logic: If query is empty, this is TRUE for all entries.
                    val searchMatch = q.isEmpty() ||
                            entry.personName.lowercase().contains(q)

                    // B. Multi-Filter Logic: All conditions must be TRUE to pass.

                    // Gender Filter: TRUE if filter.gender is NULL (default) OR if it matches entry.gender.
                    val genderMatch = filter.gender == null || entry.gender == filter.gender

                    // Temple Filter: TRUE if filter.temple is NULL (default) OR if it matches entry.templeName.
                    val templeMatch = filter.temple == null || entry.templeName == filter.temple

                    // Dharma Class Filter: TRUE if filter.isDmAttended is NULL (default) OR if it matches entry status.
                    val classMatch =
                        filter.isDmAttended == null || entry.is2DaysDharmaClassAttend == filter.isDmAttended

                    // Date Filters: TRUE if filter string is NULL (default) OR if it matches the range.
                    val startDateMatch =
                        filter.startDate.isNullOrEmpty() || entry.initiationDate >= filter.startDate
                    val endDateMatch =
                        filter.endDate.isNullOrEmpty() || entry.initiationDate <= filter.endDate

                    searchMatch && genderMatch && templeMatch && classMatch && startDateMatch && endDateMatch

                }

                when{
                    filteredList.isNotEmpty()  ->_state.value =   UiState.Success(filteredList)
                    else -> _state.value =  UiState.Success(emptyList())
                }
            }.collect()
        }

    }

    fun updateSearchQuery(query: String){
        _searchQuery.value = query
    }

    fun updateFilter(filter: FilterState){

        _filterState.value = filter
    }

}

data class FilterState(
    val gender: Gender? = null,
    val isDmAttended: Boolean? = null,
    val startDate:String? = null,
    val endDate:String? = null,
    val temple: Temple? = null
)


