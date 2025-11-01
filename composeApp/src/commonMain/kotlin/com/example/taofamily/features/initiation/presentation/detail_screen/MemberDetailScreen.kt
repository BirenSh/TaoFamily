package com.example.taofamily.features.initiation.presentation.detail_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TempleBuddhist
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.taofamily.core.ui.AppColors
import com.example.taofamily.core.ui.ScreenTopbar
import com.example.taofamily.features.initiation.domain.model.InitiationFormFiled
import com.example.taofamily.features.initiation.presentation.form_screen.InitiationFormScreen

class MemberDetailScreen(
    private val memberId: String
): Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel: DetailViewModel = getScreenModel ()
        val memberDetail by viewModel.memberDetail.collectAsState()

        val onBackPressed:()-> Unit = {
            navigator?.pop()
        }
        val onEditPressed:()-> Unit = {
            navigator?.push(InitiationFormScreen(memberDetail))
        }


        LaunchedEffect(memberId) {

            viewModel.getDetail(memberId)
        }

        DetailScreenUI(
            memberDetail =  memberDetail,
            onBackPressed = onBackPressed,
            onEditPressed = onEditPressed

        )
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DetailScreenUI(
        memberDetail: InitiationFormFiled?,
        onBackPressed: () -> Unit,
        onEditPressed: () -> Unit
    ) {
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        Scaffold(
            modifier =  Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                ScreenTopbar(
                    title = "Your Profile",
                    containerColor = Color.Transparent,
                    trailingIcon = Icons.Default.EditNote,
                    onActionClick = {
                        onEditPressed()
                    },
                    onBack = {
                        onBackPressed()
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0.dp)
        ){
            DetailContentsUi(modifier = Modifier,memberDetail)

        }
    }


    @Composable
    fun DetailContentsUi(modifier: Modifier, memberDetail: InitiationFormFiled?) {
        val scrollState = rememberScrollState()
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 50.dp)
                .verticalScroll(scrollState)
                .background(AppColors.WhiteSmoke),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp) // total height (cover + overlap)
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFFFA726), // orange
                                    Color(0xFFFF7043)  // deep orange
                                )
                            )
                        )
                )

                // --- Overlapping Profile Image ---
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 50.dp) // move slightly down to overlap nicely
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Image(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

            }

            // --- Name & Bio Section ---

            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = memberDetail?.personName?:"NA",
                style = MaterialTheme.typography.headlineMedium ,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = ("ID: " + memberDetail?.personId),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(10.dp))

            DetailCard(
                heading = "Personal Information",
                infoItem = listOf(
                    InfoItem(title = "Contact Number", value = memberDetail?.contact?:"NA", icon = Icons.Default.Call),
                    InfoItem(title = "Age", value = memberDetail?.personAge.toString()?:"NA", icon = Icons.Default.Cake),
                    InfoItem(title = "Education / Occupation", value = memberDetail?.education.toString()?:"NA", icon = Icons.Default.School),
                    InfoItem(title = "Gender", value = memberDetail?.gender?.label?:"NA", icon = Icons.Default.Person),
                    InfoItem(title = "Address", value = memberDetail?.fullAddress?:"NA", icon = Icons.Default.Home),
                )
            )

            DetailCard(
                heading = "Initiation Details",
                infoItem = listOf(
                    InfoItem(title = "Temple Name", value = memberDetail?.templeName?.label?:"NA", icon = Icons.Default.TempleBuddhist),
                    InfoItem(title = "Master Name", value = memberDetail?.masterName?.label?:"NA", icon = Icons.Default.PersonOutline),
                    InfoItem(title = "Introducer Name", value = memberDetail?.introducerName?:"NA", icon = Icons.Default.PersonAdd),
                    InfoItem(title = "Guarantor Name", value = memberDetail?.guarantorName?:"NA", icon = Icons.Default.PersonPin),
                    InfoItem(title = "Initiation Date", value = memberDetail?.initiationDate?:"NA", icon = Icons.Default.CalendarMonth),
                    InfoItem(title = "Merits Fee", value = memberDetail?.meritFee.toString(), icon = Icons.Default.Help),
                )
            )

            DetailCard(
                heading = "Dharma Class",
                infoItem = listOf(
                    InfoItem(title = "2 days Dharma class attended", value = if (memberDetail?.is2DaysDharmaClassAttend == true) "Yes" else "No", icon = Icons.Default.EventSeat),
                    InfoItem(title = "Dharma meeting date", value = memberDetail?.dharmaMeetingDate?:"NA", icon = Icons.Default.CalendarMonth),
                    )
            )

        }
    }
}


@Composable
fun DetailCard(
    modifier: Modifier = Modifier,
    heading:String,
    infoItem: List<InfoItem>
){
    Card(
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 15.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.PureWhite,
            contentColor = AppColors.TextDark)
    ){
        Column(
            modifier = modifier.padding(horizontal = 15.dp, vertical = 12.dp),
        ) {
            Text(
                text = heading,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            infoItem.forEach { item ->
                InfoRow(item)
                Spacer(modifier = Modifier.height(13.dp))
            }

        }
    }
}

@Composable
fun InfoRow(item: InfoItem) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.size(20.dp))
        Column {
            Text(
                text = item.title,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.size(2.dp))
            Text(
                text = item.value
            )
        }
    }
}

data class InfoItem(
    val title: String,
    val value: String,
    val icon: ImageVector
)