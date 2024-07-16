package com.example.applicationsocket.ui.cameraStuff

import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.applicationsocket.CameraPreview
//import com.example.applicationsocket.CameraPreview
import com.example.applicationsocket.R
import com.example.applicationsocket.ui.theme.ApplicationSocketTheme
@Composable
fun TopBar(modifier: Modifier = Modifier){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { /* Xử lý sự kiện nhấn nút 1 */ },
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_1),
                contentDescription = "Icon 1",
            )
        }

        Button(
            onClick = { /* Xử lý sự kiện nhấn nút 2 */ }
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_1),
                contentDescription = "Icon 1",
            )
            Text("Nút 2")
        }

        Button(
            onClick = { /* Xử lý sự kiện nhấn nút 3 */ },
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_3),
                contentDescription = "Icon 3",
            )
        }
    }
}

@Composable
fun BottomBar(modifier: Modifier = Modifier){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { /* Xử lý sự kiện nhấn nút 1 */ },
        ) {
            Image(
                imageVector = Icons.Default.FlashOn,
                contentDescription = "Turn on or off flash light",
            )
        }

        Button(
            onClick = { /* Xử lý sự kiện nhấn nút 2 */ }
        ) {
            Image(
                imageVector = Icons.Default.Camera,
                contentDescription = "Take picture",
            )
        }

        Button(
            onClick = {

            },
        ) {
            Image(
                imageVector = Icons.Default.Cameraswitch,
                contentDescription = "Switch camera"
            )
        }
    }
}

@Composable
fun testmain(){
    Column(modifier = Modifier.fillMaxSize()) {
        // Phần trên cùng của màn hình
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f)
                .background(Color.Red),
            contentAlignment = Alignment.Center
        ) {
            TopBar()
        }

        // Phần giữa của màn hình
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Green),
            contentAlignment = Alignment.Center
        ) {
            // có chỉnh sửa lại phần CameraPreview
            CameraPreview()
        }

        // Phần dưới cùng của màn hình
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f)
                .background(Color.Blue),
            contentAlignment = Alignment.Center
        ) {
            BottomBar()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TopBarPreview() {
    ApplicationSocketTheme {
        testmain()
    }
}
