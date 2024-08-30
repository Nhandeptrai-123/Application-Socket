package com.example.applicationsocket.ui.cameraStuff

import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.example.applicationsocket.CameraPreview
import com.example.applicationsocket.R
import com.example.applicationsocket.ui.theme.ApplicationSocketTheme
import java.io.File
import java.io.InputStream

@Composable
fun SendTo(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Gửi đến...",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White
        )
    }
}

@Composable
fun EditButtonInBottomBar(
    photoUri: Uri,
    modifier: Modifier = Modifier
){
    val backgroundColorLocket = Color(0xFF111111)
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {  },
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColorLocket,
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.close),
                contentDescription = "Turn on or turn off flash",
                modifier = Modifier
                    .size(20.dp)
            )
        }


        val borderColorTakePictureIcon = Color(0xFFefaf0c)
        FloatingActionButton(
            onClick = { },
            containerColor = Color(0xFF505050),
            shape = CircleShape,
            modifier = Modifier
                .size(95.dp) // Kích thước của nút
        ) {
            Image(
                painter = painterResource(id = R.drawable.send),
                contentDescription = "Turn on or turn off flash",
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF505050), shape = CircleShape)
            )
        }

        Button(
            onClick = {
                val resolver = context.contentResolver
                val inputStream: InputStream? = resolver.openInputStream(photoUri)
                val filename = "${System.currentTimeMillis()}.jpg"

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyApp")  // Đường dẫn lưu trữ mà không cần dùng Environment
                }

                val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                imageUri?.let { newUri ->
                    val outputStream = resolver.openOutputStream(newUri)
                    inputStream?.copyTo(outputStream!!)
                    outputStream?.close()
                    inputStream?.close()
                    Toast.makeText(context, "Đã lưu ảnh", Toast.LENGTH_SHORT).show()
                } ?: run {
                    Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColorLocket,
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.download),
                contentDescription = "Switch camera",
                modifier = Modifier
                    .size(30.dp)
                    .background(backgroundColorLocket)
            )
        }
    }
}

@Composable
fun PersonToSeePictureBlock(painterResoureForImage: Int, personName: String){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier.padding(8.dp),
    ) {
        // Tạo một trạng thái để lưu trữ trạng thái bật/tắt của nút
        val (isToggled, setToggled) = remember { mutableStateOf(false) }

        //Chua xong phan doi mau vien khi click nut
        // Sử dụng Box để tạo viền ngoài cho nút
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
//                .size(60.dp) // Kích thước cho toàn bộ Box, bao gồm cả viền (not use to have the auto resize mechanism (LIEMLEE)
                .border(
                    width = 4.dp, // Độ dày của viền ngoài
                    color = Color(0xFF2d2d2d), // Màu viền ngoài
                    shape = CircleShape
                )
                .padding(3.5.dp) // Khoảng cách giữa viền ngoài và viền trong
                .border(
                    width = 3.dp, // Độ dày của viền trong
                    color = Color(0xFF111111), // Màu viền trong
                    shape = CircleShape
                )
                .padding(3.dp) // Khoảng cách giữa viền trong và nội dung
        ) {
            FloatingActionButton(
                onClick = {
                    // Thay đổi trạng thái khi nút được nhấn
                    setToggled(!isToggled)
                },
                shape = CircleShape,
                modifier = Modifier.size(30.dp), // Kích thước của nút
                containerColor = Color(0xFF4f4f4f)
            ) {
                Image(
                    painter = painterResource(id = painterResoureForImage),
                    contentDescription = "Toggle Button",
                    modifier = Modifier
                        .size(15.dp)
                        .background(
                            color = Color(0xFF4f4f4f)
                        )
                )
            }
        }
        Text(
            text = personName,
            fontSize = 10.sp,
            color = Color(0xFF4f4f4f),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun HorizontalScrollableRow(items: List<Int>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            PersonToSeePictureBlock(
                painterResoureForImage = R.drawable.friends,
                personName = "Mọi người"
            )
        }
        items(items) { item ->
            PersonToSeePictureBlock(
                painterResoureForImage = item,
                personName = "Liem Lee"
            )
        }
    }
}

@Composable
fun LazyRowPersonToSeePicture(){
    //su dung cho toan app
    val backgroundColorLocket = Color(0xFF111111)
    Surface(color = backgroundColorLocket) {
        HorizontalScrollableRow(
            items = listOf(
                R.drawable.send,
                R.drawable.flash,
                R.drawable.close,
                R.drawable.flash,
                R.drawable.close,
                R.drawable.flash,
                R.drawable.close
            )
        )
    }
}

@Composable
fun CameraScreenEditPicture(photoUri: Uri) {
    val context = LocalContext.current
    val (currentCamera, setCurrentCamera) = remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
    val (flashEnabled, setFlashEnabled) = remember { mutableStateOf(false) }
    val (showWhiteScreen, setShowWhiteScreen) = remember { mutableStateOf(false) }
    val imageCapture = remember {
        ImageCapture.Builder().build()
    }

    val coroutineScope = rememberCoroutineScope()

    Log.e("CameraScreenEditPicture", photoUri.toString())
    Column(modifier = Modifier.fillMaxSize()) {
        //su dung cho toan app
        val backgroundColorLocket = Color(0xFF111111)
        // Phần trên cùng của màn hình
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.15625f)
                .background(backgroundColorLocket),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SendTo()
        }

        // Phần giữa của màn hình
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4375f)
                .background(backgroundColorLocket)
                .clip(RoundedCornerShape(55.dp))
        ) {
            Image(
                painter = rememberAsyncImagePainter(photoUri),
                contentDescription = "Captured Image",
                modifier = Modifier.fillMaxSize().fillMaxWidth()
            )
        }
        // Phần dưới cùng của màn hình
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.21875f)
                .background(backgroundColorLocket)
                .padding(top = 20.dp),
//            verticalAlignment = Alignment.CenterVertically
        ) {
            EditButtonInBottomBar(
                photoUri = photoUri
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColorLocket)
                .weight(0.1875f)

        ){
            LazyRowPersonToSeePicture()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CameraScreenEditPicturePreview() {
    ApplicationSocketTheme {
     CameraScreenEditPicture(Uri.fromFile(File("")))
//        PersonToSeePictureBlock(R.drawable.friends, "User Name")
    }
}
