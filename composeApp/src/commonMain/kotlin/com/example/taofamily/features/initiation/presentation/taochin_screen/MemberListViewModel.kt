package com.example.taofamily.features.initiation.presentation.taochin_screen

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.taofamily.core.utils.UiState
import com.example.taofamily.features.initiation.data.local.SettingPreFrance
import com.example.taofamily.features.initiation.data.repository.InitiationRepository
import com.example.taofamily.features.initiation.domain.model.Gender
import com.example.taofamily.features.initiation.domain.model.InitiationFormFiled
import com.example.taofamily.features.initiation.domain.model.Temple
import com.example.taofamily.features.initiation.presentation.filter_screen.AttendanceFilterOptions
import com.example.taofamily.features.initiation.presentation.filter_screen.GenderFilterOptions
import com.example.taofamily.features.initiation.presentation.filter_screen.TempleFilterOptions
import io.ktor.util.Hash.combine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.emptyList

class MemberListViewModel(
    private val initiationRepository: InitiationRepository,
    private val settingPreFrance: SettingPreFrance
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
                    val genderMatch = when (filter.genderFilter) {
                        GenderFilterOptions.ALL -> true // Show all
                        GenderFilterOptions.MALE -> entry.gender == Gender.MALE
                        GenderFilterOptions.FEMALE -> entry.gender == Gender.FEMALE
                    }
                    // Temple Filter: TRUE if filter.temple is NULL (default) OR if it matches entry.templeName.
                    val templeMatch = when(filter.temple){
                        TempleFilterOptions.ALl -> true //show all
                        TempleFilterOptions.GOLDEN_SHRINE -> entry.templeName == Temple.GOLDEN_SHRINE
                        TempleFilterOptions.HARMONY_CENTER -> entry.templeName == Temple.HARMONY_CENTER
                        TempleFilterOptions.UNITY_TEMPLE -> entry.templeName == Temple.UNITY_TEMPLE
                        }

                    // Dharma Class Filter: TRUE if filter.isDmAttended is NULL (default) OR if it matches entry status.
                    val classMatch = when (filter.attendanceFilter) {
                        AttendanceFilterOptions.ALL -> true // Show all
                        AttendanceFilterOptions.ATTENDED -> entry.is2DaysDharmaClassAttend == true
                        AttendanceFilterOptions.NOT_ATTENDED -> entry.is2DaysDharmaClassAttend == false
                    }
                    // Date Filters: TRUE if filter string is NULL (default) OR if it matches the range.
                    val startDateMatch =
                        filter.startDate.isNullOrEmpty() || entry.initiationDate.split("-").first() >= filter.startDate
                    val endDateMatch =
                        filter.endDate.isNullOrEmpty() || entry.initiationDate.split("-").first() <= filter.endDate

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

    fun logoutApp(){
        screenModelScope.launch {
            settingPreFrance.setIsLoggedIn(false)
        }
    }

}

data class FilterState(
    val genderFilter: GenderFilterOptions = GenderFilterOptions.ALL, val isDmAttended: Boolean? = null,
    val attendanceFilter: AttendanceFilterOptions = AttendanceFilterOptions.ALL,
    val startDate:String? = null,
    val endDate:String? = null,
    val temple: TempleFilterOptions = TempleFilterOptions.ALl,
)


