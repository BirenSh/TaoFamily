package com.example.taofamily.features.initiation.presentation.detail_screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class MemberDetailScreen(
    private val memberId: String
): Screen {
    @Composable
    override fun Content() {
        Text("Member Detail Screen for Member ID: $memberId")
    }
}