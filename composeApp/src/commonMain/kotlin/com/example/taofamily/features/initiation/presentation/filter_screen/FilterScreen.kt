package com.example.taofamily.features.initiation.presentation.filter_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.taofamily.core.ui.AppColors
import com.example.taofamily.core.ui.ScreenTopbar
import com.example.taofamily.core.ui.SingleSelectFilterChips
import com.example.taofamily.core.utils.UiState
import com.example.taofamily.features.initiation.domain.model.InitiationFormFiled
import com.example.taofamily.features.initiation.presentation.taochin_screen.FilterState
import com.example.taofamily.features.initiation.presentation.taochin_screen.MemberListViewModel
import org.koin.core.qualifier.named

class FilterScreen(
    private val memberViewModel: MemberListViewModel
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        val onBackPressed: () -> Unit = {
            navigator?.popUntilRoot()
        }

        // Use the current state from the ViewModel as the *initial* screen state
        val initialFilterState = memberViewModel.filterState.collectAsState().value

        // Local mutable states for user editing (user commits changes upon button press)
        var currentFilterState by remember { mutableStateOf(initialFilterState) }
        val state by memberViewModel.state.collectAsState()
        if (state is UiState.Success) {
            val count = (state as UiState.Success<List<InitiationFormFiled>>).result.size
            println("state count in filter: $count")
        }

        FilterScreenCompose(
            currentFilterState = currentFilterState,
            onUpdateFilter = { currentFilterState = it }, // Update local state on interaction
            onApplyFilters = {
                memberViewModel.updateFilter(currentFilterState) // Send final state to ViewModel
                navigator?.pop() // Close the filter modal
            },
            onClearAll = {
                currentFilterState = FilterState() //resetting to initial
                memberViewModel.updateFilter(currentFilterState) // updating initial state
                navigator?.pop() // return back after clear
            }, // Reset local state to default
            onBackPressed = onBackPressed
        )
    }


    @Composable
    fun FilterScreenCompose(
        currentFilterState: FilterState,
        onUpdateFilter: (FilterState) -> Unit,
        onApplyFilters: () -> Unit,
        onBackPressed: () -> Unit,
        onClearAll: () -> Unit,
    ) {

        // Options defined here
        val genderOptions = remember { GenderFilterOptions.entries.toList() }
        val attendanceOptions = remember { AttendanceFilterOptions.entries.toList() }
        val templeOptions = remember { TempleFilterOptions.entries.toList() }
        Scaffold(
            topBar = {
                ScreenTopbar(
                    title = "Filter",
                    onBack = {
                        onBackPressed()
                    },
                    containerColor = AppColors.TopBarBackground
                )
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    containerColor = AppColors.PureWhite
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { onClearAll() },
                            modifier = Modifier.weight(1f)
                                .padding(horizontal = 10.dp)
                                .shadow(shape = RoundedCornerShape(14.dp), elevation = 2.dp)
                                .background(AppColors.WhiteSmoke),
                        ){
                            Text("CLEAR", style = TextStyle(fontWeight = FontWeight.Medium))

                        }

                        IconButton(
                            onClick = { onApplyFilters() },
                            modifier = Modifier.weight(1f)
                                .padding(horizontal = 10.dp)
                                .shadow(shape = RoundedCornerShape(14.dp), elevation = 2.dp)
                                .background(AppColors.PrimaryOrange),
                        ){
                            Text("APPLY", style = TextStyle(fontWeight = FontWeight.Medium))
                        }
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(AppColors.WhiteSmoke)
                    .padding(10.dp),
            ) {
                //--------------------------------------Gender Filter
                Text(
                    text = "Gender",
                    color = AppColors.HintColor,
                    style = MaterialTheme.typography.titleSmall
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    SingleSelectFilterChips(
                        options = genderOptions,
                        selected = currentFilterState.genderFilter,
                        onSelectionChange = { selected ->
                            onUpdateFilter(currentFilterState.copy(genderFilter = selected))
                        }
                    )
                }

                HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp))

                ////--------------------------------------is 2 Days Dharma meeting Attended filter
                Text(
                    text = "Is 2 Days Dharma Meeting Attended ?",
                    color = AppColors.HintColor,
                    style = MaterialTheme.typography.titleSmall
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    SingleSelectFilterChips(
                        options = attendanceOptions,
                        selected = currentFilterState.attendanceFilter,
                        onSelectionChange = { selected ->
                            onUpdateFilter(currentFilterState.copy(attendanceFilter = selected))
                        }
                    )
                }

                HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp))

                ////--------------------------------------Temple filter
                Text(
                    text = "Temple Filter",
                    color = AppColors.HintColor,
                    style = MaterialTheme.typography.titleSmall
                )
                SingleSelectFilterChips(
                    options = templeOptions,
                    selected = currentFilterState.temple,
                    onSelectionChange = { selected ->
                        onUpdateFilter(currentFilterState.copy(temple = selected))
                    }
                )

                HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp))

                ////--------------------------------------Date Range filter
                Text(
                    text = "Year Range Filter",
                    color = AppColors.HintColor,
                    style = MaterialTheme.typography.titleSmall
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {

                    // input for start year
                    YearRangeInput(
                        dateValue = currentFilterState.startDate.orEmpty(),
                        label = "Start Year",
                        modifier = Modifier.padding(vertical = 10.dp),
                        onValueChange = { date ->
                            val sanitizedRawDigits = date.filter { it.isDigit() }.take(4)
                                onUpdateFilter(currentFilterState.copy(startDate = sanitizedRawDigits))
                        }
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    //input for end year
                    YearRangeInput(
                        dateValue = currentFilterState.endDate.orEmpty(),
                        label = "End Year",
                        modifier = Modifier.padding(vertical = 10.dp),
                        onValueChange = {date->
                            val sanitizedRawDigits = date.filter { it.isDigit() }.take(4)
                                onUpdateFilter(currentFilterState.copy(endDate = sanitizedRawDigits))

                        }
                    )
                }


            }
        }
    }


}


@Composable
fun YearRangeInput(
    label: String,
    modifier: Modifier,
    dateValue: String,
    onValueChange: (String) -> Unit
) {
    // We use Box to stack the placeholder text and the BasicTextField
    Box(
        modifier = modifier
            .wrapContentSize()
            .border(
                border = BorderStroke(1.dp, AppColors.LightBlack),
                shape = RoundedCornerShape(15.dp) // Custom border and shape
            )
            .background(Color.Transparent, shape = RoundedCornerShape(15.dp))
            .padding(horizontal = 16.dp), // Padding inside the border
        contentAlignment = Alignment.CenterStart // Centers content vertically,

    ) {

        // 1. Placeholder (Only visible if the value is empty)
        if (dateValue.isEmpty()) {
            Text(
                text = label,
            )
        }

        // 2. The Basic Input Component
        BasicTextField(
            value = dateValue,
            onValueChange =  onValueChange ,
            modifier = Modifier.padding(vertical =10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,imeAction = ImeAction.Done),
            textStyle = TextStyle(fontWeight = FontWeight.Medium)
        )
    }
}


