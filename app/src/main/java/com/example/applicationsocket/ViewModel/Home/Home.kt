package com.example.applicationsocket.ViewModel.Home

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.example.applicationsocket.UserSessionViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.applicationsocket.data.modelNameUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(userViewModel: UserSessionViewModel) {
    val userInformation = userViewModel.userInformation.observeAsState().value

    if (userInformation != null) {
        Text(text = "Welcome, ${userInformation.firstName} ${userInformation.lastName}")
    } else {
        Text(text = "No user information available")
        Log.d("Home", "No user information available")
    }
}