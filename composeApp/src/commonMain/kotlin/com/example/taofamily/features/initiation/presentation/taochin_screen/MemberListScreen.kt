package com.example.taofamily.features.initiation.presentation.taochin_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.taofamily.core.ui.AppColors
import com.example.taofamily.core.ui.AppSearchBar
import com.example.taofamily.core.ui.ScreenTopbar
import com.example.taofamily.core.utils.UiState
import com.example.taofamily.features.initiation.domain.model.InitiationFormFiled
import com.example.taofamily.features.initiation.presentation.detail_screen.MemberDetailScreen
import com.example.taofamily.features.initiation.presentation.filter_screen.FilterScreen
import com.example.taofamily.features.initiation.presentation.form_screen.InitiationFormScreen
import com.example.taofamily.features.initiation.presentation.login_screen.LoginScreen

class MemberListScreen() : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        val memberViewModel : MemberListViewModel = getScreenModel()

        // locally navigation action
        val onFilterScreenClick:()->Unit = {
            navigator?.push(FilterScreen(memberViewModel))
        }
        val onDetailScreenClick: (memberId: String)-> Unit = {id->
            navigator?.push(MemberDetailScreen(memberId = id))
        }
        val onInitiationFormScreen: () -> Unit = {
            navigator?.push(InitiationFormScreen(entry = null))
        }

        val onSettingClick : ()-> Unit = {
            memberViewModel.logoutApp()
            navigator?.replaceAll(LoginScreen())
        }

        val searchQuery by memberViewModel.searchQuery.collectAsState()

        val onSearchQueryChange: (String) -> Unit = {
            memberViewModel.updateSearchQuery(it)
        }



        val state by memberViewModel.state.collectAsState()


        MemberListScreenCompose(
            onFilterScreenClick = onFilterScreenClick,
            onDetailScreenClick = onDetailScreenClick,
            onInitiationFormScreen = onInitiationFormScreen,
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            currentState = state,
            onSettingClick = onSettingClick
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MemberListScreenCompose(
        onFilterScreenClick: () -> Unit,
        onDetailScreenClick: (String) -> Unit,
        onInitiationFormScreen: () -> Unit,
        onSearchQueryChange: (String) -> Unit,
        currentState: UiState<List<InitiationFormFiled>>,
        searchQuery: String,
        onSettingClick: () -> Unit,

        ) {

        var count = 0
        if (currentState is UiState.Success){
            count = (currentState ).result.size
        }


        Scaffold(
            topBar = {
                ScreenTopbar(
                    title = "Tao Family",
                    containerColor = AppColors.TopBarBackground,
                    trailingIcon = Icons.Default.Add,
                    onActionClick = {
                        onInitiationFormScreen()
                    }
                )
            },
            bottomBar = {

                BottomAppBar(
                    containerColor = AppColors.TopBarBackground,
                    contentColor = AppColors.PrimaryBlack,
                    modifier = Modifier.height(100.dp),
                    content = {
                       Row(modifier = Modifier.fillMaxWidth(),
                           horizontalArrangement = Arrangement.SpaceAround) {
                           IconButton(
                               onClick = {}
                           ){
                               Icon(
                                   imageVector = Icons.Default.Home,
                                   contentDescription = "Item list",
                                   tint = Color.White
                               )
                           }
                           IconButton(
                               onClick = onFilterScreenClick
                           ){
                               Icon(
                                   imageVector = Icons.Default.FilterList,
                                   contentDescription = "Filter"
                               )
                           }

                           IconButton(
                               onClick = onSettingClick
                           ){
                               Icon(
                                   imageVector = Icons.Default.Settings,
                                   contentDescription = "Setting"
                               )
                           }

                           TextButton(onClick = {}){
                               Text(count.toString(), color = Color.Black)
                           }

                       }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                Modifier.padding(paddingValues)
                    .fillMaxSize()
                    .background(AppColors.WhiteSmoke)
            ) {

                AppSearchBar(
                    query = searchQuery,
                    onQueryChange = { onSearchQueryChange(it) },
                    placeHolder = "Search by Name",
                    modifier = Modifier.fillMaxWidth().padding(10.dp)
                )


                when(val state = currentState){
                    is UiState.Loading ->{

                    }
                    is UiState.Ideal ->{

                    }
                    is UiState.Error ->{

                        state.errorMessage
                    }
                    is UiState.Success ->{
                        val data =  state.result
                        //Display the member
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            items(data.size){member->
                                MemberCard(
                                    member = data[member],
                                    onDetailScreenClick = onDetailScreenClick
                                )

                            }
                        }
                    }else -> {
                        // do nothing
                    }
                }
            }
        }

    }

    @Composable
    fun MemberCard(
        member: InitiationFormFiled,
        onDetailScreenClick: (String) -> Unit
    ) {
        Card(
            onClick = {onDetailScreenClick(member.personId)},
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppColors.MemberCardBorder,
            ),
            elevation = CardDefaults.cardElevation(2.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            shape = CardDefaults.shape
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    //member name
                    Text(
                        text = member.personName,
                        style = MaterialTheme.typography.titleMedium,
                        color = AppColors.TextDark,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    //initiation date with icon
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = "initiation date",
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Initiation Date: ${member.initiationDate}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.LightBlack
                        )
                    }
                }
                // dharma meeting attended status
                val isAttended = member.is2DaysDharmaClassAttend
                if (isAttended) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        tint = AppColors.LightBlack,
                        contentDescription = "Dharma Meeting Attended",
                    )
                }
            }


        }
    }



}