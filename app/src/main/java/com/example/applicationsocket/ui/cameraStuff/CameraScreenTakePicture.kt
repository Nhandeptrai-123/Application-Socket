package com.example.applicationsocket.ui.cameraStuff

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.applicationsocket.Greeting
import com.example.applicationsocket.R
import com.example.applicationsocket.ui.theme.ApplicationSocketTheme

@Composable
fun CircularImage(painter: Painter, contentDescription: String){

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            shape = CircleShape,
            border = BorderStroke(2.dp, Color.White)
        ) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
            )
        }
    }
}

@Composable
fun test1(painter: Painter, contentDescription: String){
    Card(
        shape = CircleShape,
        border = BorderStroke(2.dp, Color.White)
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
        )
    }
}

@Composable
fun TopBar(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        test1(painter = painterResource(id = R.drawable.icon_3), contentDescription = "test" )
        Button(
            onClick = { /* Xử lý sự kiện nhấn nút 1 */ },
            shape = CircleShape,
            modifier = Modifier.size(64.dp)
        ) {
            CircularImage(
                painter = painterResource(id = R.drawable.icon_1),
                contentDescription = "Nút 1"
            )
        }

        Button(
            onClick = { /* Xử lý sự kiện nhấn nút 2 */ }
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_1),
                contentDescription = "Icon 1",
//                modifier = Modifier.size(32.dp)
            )
            Text("Nút 2")
        }

        Button(
            onClick = { /* Xử lý sự kiện nhấn nút 3 */ },
//            modifier = Modifier.size(48.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_3),
                contentDescription = "Icon 3",
//                modifier = Modifier.size(.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    ApplicationSocketTheme {
        TopBar()
    }
}
