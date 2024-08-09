package com.example.applicationsocket.ui.cameraStuff

import android.net.Uri
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
    modifier: Modifier = Modifier
){
    val backgroundColorLocket = Color(0xFF111111)
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
            onClick = { },
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
                modifier = Modifier.fillMaxSize()
            )
//            CameraPreview(currentCamera, onImageCaptured = { uri ->
//                // Handle captured image URI here
//            }, imageCapture)
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
            EditButtonInBottomBar()
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

//@Composable
//fun testmain1(){
//    val context = LocalContext.current
//    val (currentCamera, setCurrentCamera) = remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
//    val (flashEnabled, setFlashEnabled) = remember { mutableStateOf(false) }
//    val (showWhiteScreen, setShowWhiteScreen) = remember { mutableStateOf(false) }
//    val imageCapture = remember {
//        ImageCapture.Builder().build()
//    }
//
//    val coroutineScope = rememberCoroutineScope()
//
//    fun capturePhoto() {
//
//        if (flashEnabled && currentCamera == CameraSelector.DEFAULT_BACK_CAMERA) {
//            imageCapture.flashMode = ImageCapture.FLASH_MODE_ON
//        } else {
//            imageCapture.flashMode = ImageCapture.FLASH_MODE_OFF
//        }
//
//        // Tạo một tệp tin để lưu trữ ảnh
//        val photoFile = File(
//            context.getExternalFilesDir(null),
//            "${System.currentTimeMillis()}.jpg"
//        )
//
//        // Thiết lập các tùy chọn đầu ra cho ImageCapture
//        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//
//        // Chụp ảnh và lưu trữ nó vào tệp tin đã tạo
//        imageCapture.takePicture(
//            outputOptions,
//            ContextCompat.getMainExecutor(context),
//            object : ImageCapture.OnImageSavedCallback {
//                // Xử lý khi chụp ảnh thất bại
//                override fun onError(exception: ImageCaptureException) {
//                    Log.e("CameraPreview", "Photo capture failed: ${exception.message}", exception)
//                }
//
//                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                    // Lấy URI của ảnh đã lưu
//                    val savedUri = Uri.fromFile(photoFile)
//                    Log.d("CameraPreview", "Photo capture succeeded: $savedUri")
//                    // Handle the saved image URI here
//                    // Xử lý URI của ảnh đã lưu ở đây
//                    Toast.makeText(context, "Photo capture succeeded: $savedUri", Toast.LENGTH_SHORT).show()
//                }
//            }
//        )
//    }
//
//    fun takePicture() {
//        if (flashEnabled && currentCamera == CameraSelector.DEFAULT_FRONT_CAMERA) {
//            setShowWhiteScreen(true)
//            coroutineScope.launch {
//                delay(1000)
//                capturePhoto()
//                setShowWhiteScreen(false)
//            }
//        } else {
//            capturePhoto()
//        }
//    }
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        // Phần trên cùng của màn hình
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(0.15625f)
//                .background(Color.Red),
////            contentAlignment = Alignment.Center
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            if (showWhiteScreen) {
//                Box(modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.White))
//            } else {
//                TopBar()
//            }
//        }
//
//        // Phần giữa của màn hình
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(0.4375f)
//                .background(Color.Green)
//                .clip(RoundedCornerShape(55.dp)),
////            contentAlignment = Alignment.Center
//        ) {
//            CameraPreview(
//                currentCamera,
//                onImageCaptured = { uri ->
//                // Handle captured image URI here
//                },
//                imageCapture,
//                modifier = Modifier
////                        .align(Alignment.Center)
//            )
//        }
//
//        // Phần dưới cùng của màn hình
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(0.375f)
//                .background(Color.Blue),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            if (showWhiteScreen) {
//                Box(modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.White))
//            } else {
//                BottomBar(
//                    onTakePicture = { takePicture() },
//                    onSwitchCamera = {
//                        setCurrentCamera(
//                            if (currentCamera == CameraSelector.DEFAULT_BACK_CAMERA)
//                                CameraSelector.DEFAULT_FRONT_CAMERA
//                            else
//                                CameraSelector.DEFAULT_BACK_CAMERA
//                        )
//                    },
//                    flashEnabled = flashEnabled,
//                    onFlashToggle = { setFlashEnabled(!flashEnabled) }
//                )
//            }
//        }
//    }
//}

//@Composable
//fun testmain(){
//    Column(modifier = Modifier.fillMaxSize()) {
//        // Phần trên cùng của màn hình
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(2f)
//                .background(Color.Red),
//            contentAlignment = Alignment.Center
//        ) {
//            TopBar()
//        }
//
//        // Phần giữa của màn hình
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(4f)
//                .background(Color.Green),
//            contentAlignment = Alignment.Center
//        ) {
//            // có chỉnh sửa lại phần CameraPreview
//            CameraPreview()
//        }
//
//        // Phần dưới cùng của màn hình
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(3.25f)
//                .background(Color.Blue),
//            contentAlignment = Alignment.Center
//        ) {
//            BottomBar()
//        }
//    }
//}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CameraScreenEditPicturePreview() {
    ApplicationSocketTheme {
//        SendTo()
//        EditButtonInBottomBar()
//        LazyRowPersonToSeePicture()
        PersonToSeePictureBlock(R.drawable.friends, "User Name")
    }
}
