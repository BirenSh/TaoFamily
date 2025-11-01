package com.example.taofamily.features.initiation.presentation.form_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDownCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.Workspaces
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.taofamily.core.ui.AppColors
import com.example.taofamily.core.ui.ErrorDialog
import com.example.taofamily.core.ui.FormCheckBox
import com.example.taofamily.core.ui.FormDropdown
import com.example.taofamily.core.ui.FormInputText
import com.example.taofamily.core.ui.LoadingDialog
import com.example.taofamily.core.ui.ScreenTopbar
import com.example.taofamily.core.utils.AppConstant
import com.example.taofamily.core.utils.DateUtils.formatRawDateString
import com.example.taofamily.core.utils.DateVisualTransformation
import com.example.taofamily.core.utils.UiState
import com.example.taofamily.features.initiation.domain.model.Gender
import com.example.taofamily.features.initiation.domain.model.InitiationFormFiled
import com.example.taofamily.features.initiation.domain.model.Master
import com.example.taofamily.features.initiation.domain.model.Temple

class InitiationFormScreen(
    private val entry: InitiationFormFiled?
): Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val formViewModel: InitiationFormViewModel = getScreenModel()

        val formData by formViewModel.formData.collectAsState()

        val isFormValid by  formViewModel.isFormValid.collectAsState()

        LaunchedEffect(Unit){
            formViewModel.loadInitialData(entry)
        }

        val onBackClick: () -> Unit = {
            navigator?.popUntilRoot()
        }

        val onSubmitClick: () -> Unit = {
            formViewModel.onSubmitClick()
        }

        val updateListener: (InitiationFormFiled) -> Unit = { data ->
            formViewModel.updateForm(data)
        }

        val submitFormState by formViewModel.submitFormState.collectAsState()

        val dismissError = { formViewModel.resetSaveState() }

        LoadingDialog(
            isVisible = submitFormState is UiState.Loading,
            labelText = "Submitting Form",
            onDismissCall = dismissError

        )

        ErrorDialog(
            isVisible = submitFormState is UiState.Error,
            errorMessage = (submitFormState as? UiState.Error)?.errorMessage?:"Something went wrong",
            onDismissCall = dismissError
        )

        LaunchedEffect(submitFormState){
            if (submitFormState is UiState.Success){
                navigator?.popUntilRoot()
                formViewModel.resetSaveState()
            }
        }


        InitiationFormCompose(
            onBackClick = onBackClick,
            formState = formData,
            updateListener = updateListener,
            onSubmitClick= onSubmitClick,
            isFormValid,


        )


    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun InitiationFormCompose(
        onBackClick: () -> Unit,
        formState: InitiationFormFiled,
        updateListener: (InitiationFormFiled) -> Unit,
        onSubmitClick: () -> Unit,
        isFormValid: Boolean,
        ) {
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                ScreenTopbar(
                    title = "Initiation Form",
                    onBack = {
                        onBackClick()
                    },
                    containerColor = AppColors.TopBarBackground,
                    scrollBehavior = scrollBehavior


                    )
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(innerPadding)
                    .verticalScroll(state = rememberScrollState())
                    .background(AppColors.WhiteSmoke)
            ) {
                InitiationFormInputs(
                    formState = formState,
                    updateListener = updateListener,
                    isFormValid
                )
                Spacer(modifier = Modifier.width(12.dp))

                ElevatedButton(
                    onClick = { onSubmitClick() },
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = ButtonDefaults.buttonElevation(),
                    colors = ButtonColors(
                        containerColor = AppColors.PrimaryOrange,
                        contentColor = AppColors.PrimaryBlack,
                        disabledContainerColor = AppColors.DisabledColor,
                        disabledContentColor = AppColors.PrimaryBlack

                    ),
                    enabled = isFormValid
                ) {
                    Text("Submit Form")
                }

            }
        }
    }


    @Composable
    private fun InitiationFormInputs(
        formState: InitiationFormFiled,
        updateListener: (InitiationFormFiled) -> Unit,
        isFormValid: Boolean
    ) {
        FormInputText(
            value = formState.personName,
            onValueChange = { name ->
                updateListener(formState.copy(personName = name))
            },
            placeHolder = "Member Name",
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Default.Person
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            FormInputText(
                value = if (formState.personAge == 0) "" else formState.personAge.toString(),
                onValueChange = { age ->
                    updateListener(formState.copy(personAge = age.toIntOrNull() ?: 0))
                },
                placeHolder = "Member Age",
                modifier = Modifier.weight(1f),
                keyboardType = KeyboardType.Number,
                icon = Icons.Default.Numbers,
                maxChar = 3
            )
//                    Spacer(modifier = Modifier.width(5.dp))

            //gender list
            val genderOptions = remember { Gender.entries.toList() }
            FormDropdown(
                onValueSelected = {
                    updateListener(formState.copy(gender = it))
                },
                selectedValue = formState.gender,
                label = "Gender",
                options = genderOptions,
                icon = Icons.Default.ArrowDropDownCircle,
                modifier = Modifier.weight(1f),
            )
        }
        FormInputText(
            value = formState.education,
            onValueChange = { education ->
                updateListener(formState.copy(education = education))
            },
            placeHolder = "Education or Occupation",
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Default.Workspaces
        )

        FormInputText(
            value = formState.fullAddress,
            onValueChange = { address ->
                updateListener(formState.copy(fullAddress = address))
            },
            placeHolder = "Full Address",
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Default.MyLocation
        )

        FormInputText(
            value = formState.contact,
            onValueChange = { contact ->
                updateListener(formState.copy(contact = contact))
            },
            placeHolder = "Member Contact number",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Number,
            icon = Icons.Default.Contacts,
            maxChar = 10
        )

        //master name list
        val masterNamesOptions = remember { Master.entries.toList() }
        FormDropdown(
            selectedValue = formState.masterName,
            onValueSelected = {
                updateListener(formState.copy(masterName = it))
            },
            label = "Master Name",
            options = masterNamesOptions,
            icon = Icons.Default.ArrowDropDownCircle,
            modifier = Modifier.fillMaxWidth()
        )

        FormInputText(
            value = formState.introducerName,
            onValueChange = { introducer ->
                updateListener(formState.copy(introducerName = introducer))
            },
            placeHolder = "Enter Introducer Name",
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Default.PersonOutline
        )

        FormInputText(
            value = formState.guarantorName,
            onValueChange = { guarantor ->
                updateListener(formState.copy(guarantorName = guarantor))
            },
            placeHolder = "Enter Guarantor Name",
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Default.PersonPin

        )

        //Temple name list
        val templeOption = remember { Temple.entries.toList() }
        FormDropdown(
            selectedValue = formState.templeName,
            onValueSelected = {
                updateListener(formState.copy(templeName = it))
            },
            label = "Master Name",
            options = templeOption,
            icon = Icons.Default.ArrowDropDownCircle,
            modifier = Modifier.fillMaxWidth()
        )

        FormInputText(
            value = if (formState.meritFee == 0.0) "" else formState.meritFee.toString(),
            onValueChange = { fee ->
                val onlyDigit = fee.filter { it.isDigit() }
                updateListener(formState.copy(meritFee = fee.toDoubleOrNull() ?: 100.0))
            },
            placeHolder = "Merit Fee Amount",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Number,
            icon = Icons.Default.Money
        )

        //date
        FormInputText(
            value = formState.initiationDate,
            onValueChange = { rawInput ->
                // 1. Sanitize input to get only the max 8 digits (YYYYMMDD)
                val sanitizedRawDigits = rawInput.filter { it.isDigit() }.take(8)

                // 2. Format the raw input into the final YYYY-MM-DD string
                val formattedDate = formatRawDateString(sanitizedRawDigits)

                // 3. Update the form state with the CORRECT, FORMATTED string
                updateListener(formState.copy(initiationDate = formattedDate))
            },
            placeHolder = "initiation Date YYYY-MM-DD",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Number,
            icon = Icons.Default.CalendarMonth,
            visualTransformation = DateVisualTransformation(),
            maxChar = 15,
            imeAction = ImeAction.Done

            )

        // checkbox for dharma meeting attendance
        FormCheckBox(
            checked = formState.is2DaysDharmaClassAttend,
            onCheckChange = {
                updateListener(formState.copy(is2DaysDharmaClassAttend = it))
            },
            label = "2 Days Dharma Class Attendance",
            modifier = Modifier.fillMaxWidth()
        )
        if (formState.is2DaysDharmaClassAttend){
            //DM date only if attended 2 days class
            FormInputText(
                value = formState.dharmaMeetingDate,
                onValueChange = { rawInput ->
                    // 1. Sanitize input to get only the max 8 digits (YYYYMMDD)
                    val sanitizedRawDigits = rawInput.filter { it.isDigit() }.take(8)

                    // 2. Format the raw input into the final YYYY-MM-DD string
                    val formattedDate = formatRawDateString(sanitizedRawDigits)

                    // 3. Update the form state with the CORRECT, FORMATTED string
                    updateListener(formState.copy(dharmaMeetingDate = formattedDate))
                },
                placeHolder = "DM Date YYYY-MM-DD",
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Number,
                icon = Icons.Default.CalendarMonth,
                visualTransformation = DateVisualTransformation(),
                )
        }else{
            updateListener(formState.copy(dharmaMeetingDate = ""))
        }
    }
}




