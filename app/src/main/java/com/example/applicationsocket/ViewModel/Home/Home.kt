package com.example.applicationsocket.ViewModel.Home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.applicationsocket.data.UserIDModel
import com.example.applicationsocket.data.UserSessionViewModel
import com.example.applicationsocket.data.getUserEmail
import com.example.applicationsocket.data.modelNameUser
import com.example.applicationsocket.data.modelUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(userID: String, userViewModel: UserSessionViewModel,idmodelUser: UserIDModel, toProfile: () -> Unit) {
    val userInformation = userViewModel.userInformation.observeAsState().value

    getUserEmail(userID, idmodelUser)
    val userid = idmodelUser.userID.observeAsState().value
    Column {
        if (userInformation != null ) {
            Text(text = "Welcome, ${userInformation.firstName} ${userInformation.lastName} and ${userID} và email: ${userid?.email}")
        } else {
            Text(text = "No user information available")
            Log.d("Home", "No user information available")
        }
        Button(onClick = { toProfile() }) {
            Text(text = "Đêsn Profile")
        }
    }

}