package com.example.taofamily.features.initiation.presentation.syncScreen

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.taofamily.core.ui.AppColors
import com.example.taofamily.core.utils.UiState
import com.example.taofamily.features.initiation.presentation.taochin_screen.MemberListScreen

class FirstTimeSyncScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: SyncViewModel = getScreenModel()
        val navigator = LocalNavigator.current

        val isSyncState by viewModel.isFirstTimeSyncComplete.collectAsState()

        val onCompleteClick :() -> Unit  = {
            navigator?.replace(MemberListScreen())
            viewModel.resetState()

        }
        val onRetryClick :() -> Unit  = {
            viewModel.startSyncProcess()
        }

        LaunchedEffect(Unit) {
            viewModel.startSyncProcess()
        }



        FirstTimeSyncScreenUi(
            isSyncState = isSyncState,
            onCompleteClick = onCompleteClick,
            onRetryClick = onRetryClick
        )
    }


    @Composable
    fun FirstTimeSyncScreenUi(
        isSyncState: UiState<Boolean>,
        onCompleteClick: () -> Unit,
        onRetryClick: () -> Unit
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "syncAnim")
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = LinearEasing)
            ),
            label = "rotation"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F8FA)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(10.dp)
            ) {

                when (val syncState = isSyncState) {
                    is UiState.Loading -> {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .graphicsLayer(rotationZ = rotation),
                            tint = AppColors.PrimaryOrange
                        )
                        Text("Syncing...")
                    }

                    is UiState.Success -> {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF34C759),
                            modifier = Modifier.size(80.dp)
                        )
                        Text("Sync Completed")
                        onCompleteClick()
                    }

                    is UiState.Error -> {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(80.dp)
                        )
                        Text("Sync Error: ${syncState.errorMessage}")
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                        ) {
                            Text("Retry")
                            IconButton(
                                onClick = {
                                    onRetryClick()
                                },
                            ) {
                                Icon(imageVector = Icons.Filled.Sync, contentDescription = null)
                            }
                        }

                    }

                    else -> {}
                }
            }
        }
    }

}