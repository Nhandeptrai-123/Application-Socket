package com.example.applicationsocket

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun test1(onClickAction: () -> Unit) {
    Text(text = "Hello World",
        modifier = Modifier.clickable(onClick = onClickAction)
        )
}