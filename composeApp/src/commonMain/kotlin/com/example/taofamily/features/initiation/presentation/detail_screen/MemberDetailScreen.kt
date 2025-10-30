package com.example.taofamily.features.initiation.presentation.detail_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.example.taofamily.core.ui.AppColors
import com.example.taofamily.core.ui.ScreenTopbar
import org.jetbrains.compose.resources.painterResource

class MemberDetailScreen(
    private val memberId: String
): Screen {
    @Composable
    override fun Content() {
        DetailScreenUI()
    }


    @Composable
    fun DetailScreenUI(){
        Scaffold(
            topBar = {
                ScreenTopbar(
                    title = "Your Profile",
                    containerColor = Color.Transparent,
                    trailingIcon = Icons.Default.EditNote,
                    onActionClick = {

                    },
                    onBack = {

                    }
                )
            },
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0.dp)
        ){
            DetailContentsUi(modifier = Modifier)

        }
    }


    @Composable
    fun DetailContentsUi(modifier: Modifier) {
        val scrollState = rememberScrollState()
        Column(
            modifier = modifier
                .fillMaxSize()
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
                        imageVector = Icons.Default.EditNote,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

            }
            // --- Name & Bio Section ---
            Spacer(modifier = Modifier.height(70.dp))
            Text(
                text = "Caroline Steele",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Photographer and Artist",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

        }
    }
}