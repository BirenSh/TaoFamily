package com.example.taofamily.features.initiation.presentation.form_screen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.taofamily.core.utils.AppConstant
import com.example.taofamily.core.utils.UiState
import com.example.taofamily.features.initiation.data.repository.InitiationRepository
import com.example.taofamily.features.initiation.domain.model.Gender
import com.example.taofamily.features.initiation.domain.model.InitiationFormFiled
import com.example.taofamily.features.initiation.domain.model.Master
import com.example.taofamily.features.initiation.domain.model.Temple
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InitiationFormViewModel(
    private val initiationRepository: InitiationRepository,

) : ScreenModel {
    private val _form = MutableStateFlow(InitiationFormFiled.empty())
    val formData: StateFlow<InitiationFormFiled> = _form.asStateFlow()

    private val _isFormValid = MutableStateFlow<Boolean>(false)
    val isFormValid: StateFlow<Boolean> = _isFormValid

    private val _submitFormState = MutableStateFlow<UiState<String>>(UiState.Ideal)
    val submitFormState: StateFlow<UiState<String>> = _submitFormState.asStateFlow()



    fun updateForm(newEntry: InitiationFormFiled) {
        _form.value = newEntry

//        validate each form on typing
        _isFormValid.value = formValidation(_form.value)
    }

    fun onSubmitClick() {
        screenModelScope.launch {
            val finalData = _form.value
            val formError = dateNumValidation(finalData)

            //validation check
            if(!formError.first){
                _submitFormState.value = UiState.Error(formError.second)
                return@launch
            }


            _submitFormState.value = UiState.Loading
            // api call and local save
            try {
                initiationRepository.saveEntry(finalData)
                // if no exception mean success
                _submitFormState.value = UiState.Error("Failed to Save data")

//                _submitFormState.value = UiState.Success(AppConstant.SUCCESS_RESULT)

            }catch (e: Exception){
                _submitFormState.value = UiState.Error(e.message?:"Failed to Save data")
            }

        }
    }

    private fun formValidation(data: InitiationFormFiled): Boolean {
        val baseValidation =  data.personName.isNotEmpty() &&
                (data.personAge > 0  && data.personAge < 200) &&
                data.gender != Gender.NONE &&
                data.contact.isNotEmpty() &&
                data.education.isNotEmpty() &&
                data.fullAddress.isNotEmpty() &&
                data.masterName != Master.NONE &&
                data.introducerName.isNotEmpty() &&
                data.guarantorName.isNotEmpty() &&
                data.templeName != Temple.NONE &&
                data.initiationDate.isNotEmpty() &&
                data.meritFee > 0.0

        val dmDateValidation =  if (data.is2DaysDharmaClassAttend) {
            data.dharmaMeetingDate.isNotEmpty()
        } else {
            true // If attendance is false, this part of the validation passes immediately.
        }

        return baseValidation && dmDateValidation
    }

    private fun dateNumValidation(finalData: InitiationFormFiled): Pair<Boolean, String>{
        //1 check base the filed is added
        if (!_isFormValid.value) return Pair(false, "Form is not valid")

        //2 check the contact
        if (finalData.contact.length != 10) return Pair(false, "Contact must be 10 digits")

        //3 check the date digit (yyyy-mm-dd)
        if (finalData.initiationDate.length != 10) return Pair(false, "Check Initiation Date input")

        //get the date month day and validate correct input
        val initiationDateParts = finalData.initiationDate.split("-")
        if (initiationDateParts[0].toInt() < 2000 || initiationDateParts[1].toInt() > 12 || initiationDateParts[2].toInt() > 31) return Pair(false, "Check Initiation Date input")

        // check Dharma meeting date only if attended 2 days class
        if (finalData.is2DaysDharmaClassAttend ) {
            val dmDateParts = finalData.initiationDate.split("-")
            if (dmDateParts.size < 3 || dmDateParts[0].toInt() < 2000 || dmDateParts[1].toInt() > 12 ||dmDateParts[2].toInt() > 31) return Pair(false, "Check Dharma meeting Date input")
        }

        return Pair(true, "Validation passed")

    }


    fun resetSaveState(){
        _submitFormState.value = UiState.Ideal
    }
}