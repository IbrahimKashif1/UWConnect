package com.uwconnect.android.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun NavigationHelper(
    shouldNavigate: () -> Boolean,
    destination: () -> Unit
) {
    LaunchedEffect(shouldNavigate()) {
        if (shouldNavigate()) {
            destination()
        }
    }
}