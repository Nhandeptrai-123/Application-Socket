package com.example.applicationsocket.ViewModel.Home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import androidx.room.util.query
import com.example.applicationsocket.R
import com.example.applicationsocket.ViewModel.ProfileUser.CircularImage
import com.example.applicationsocket.ViewModel.ProfileUser.bodyScreen
import com.example.applicationsocket.ViewModel.ProfileUser.getImageProfileUser
import com.example.applicationsocket.ViewModel.ProfileUser.headScreen
import com.example.applicationsocket.ViewModel.ProfileUser.uploadData
import com.example.applicationsocket.ViewModel.ProfileUser.uploadPass
import com.example.applicationsocket.data.UserIDModel
import com.example.applicationsocket.data.UserSessionViewModel
import com.example.applicationsocket.data.modelNameUser
import com.example.applicationsocket.data.modelUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


@Composable
fun bodyListFriendScreen(userIdModel: UserIDModel, userid: String){
    Column(modifier = Modifier.fillMaxSize()) {
        // Các thành phần khác của BodyScreen

        Spacer(modifier = Modifier.weight(1f)) // Đẩy các thành phần khác lên trên

        // Tích hợp FriendScreen vào cuối BodyScreen
        FriendScreen(userIdModel, userid)
    }
}
@Composable
fun FriendScreen(userIdModel: UserIDModel, userid: String) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Tìm kiếm bạn bè", "Danh sách bạn bè")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTabIndex == index) Color.Black else Color.Gray
                        )
                    }
                )
            }
        }

        when (selectedTabIndex) {
//            1 -> ListFriendScreen(userIdModel, userid)
            0 -> SearchFriendScreen(userIdModel, userid)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFriendScreen(userIdModel: UserIDModel, userid: String) {
    val userInformation = userIdModel.userID.observeAsState().value
    var search by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<modelUser>>(emptyList()) }
    val context = LocalContext.current
    val searchEmail = remember { mutableStateOf("") }

    Column {
        Row(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = search,
                onValueChange = {
                    search = it
                },
                label = { Text("Tìm kiếm", color = Color(0xFFb4b4b4)) },
                modifier = Modifier
                    .weight(0.8f) // Chiếm 80% chiều rộng
                    .padding(end = 8.dp),
                textStyle = TextStyle(color = Color.White),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFF616161),
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
            )
            FloatingActionButton(
                onClick = {
                    if(search == userInformation?.email){
                        Toast.makeText(context, "Không thể tìm kiếm chính mình", Toast.LENGTH_SHORT).show()
                        return@FloatingActionButton
                    }else if(search != userInformation?.email){
                        searchEmail.value = search
                        searchForUsers(search) { results -> searchResults = results }
                    }


                },
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.CenterVertically), // Align the FAB vertically
                containerColor = Color(0xFF111111),
                contentColor = Color.White
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search"
                )
            }
        }
        listFriend(searchResults,searchEmail.value)

    }
}
fun encodeEmail(email: String): String {
    return email.replace(".", ",")
}

fun getImageFriend(searchEmail: String, onSuccess: (modelNameUser?) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val encodedEmail = encodeEmail(searchEmail) // Encode email to be a valid Firebase path
    val userRef = database.getReference("users").child(encodedEmail).child("information")

    userRef.get()
        .addOnSuccessListener { snapshot ->
            val imageProfileUser = snapshot.child("imageProfileUser").getValue(String::class.java)
            val firstName = snapshot.child("firstName").getValue(String::class.java)
            val lastName = snapshot.child("lastName").getValue(String::class.java)

            val userInformation = modelNameUser(
                imageProfileUser = imageProfileUser,
                firstName = firstName,
                lastName = lastName
            )
            onSuccess(userInformation)
        }
        .addOnFailureListener { exception ->
            Log.e("FirebaseDatabase", "Error getting data ImageUser", exception)
            onSuccess(null)
        }
}


@Composable
fun listFriend(searchResults: List<modelUser>,searchEmail : String) {
    var userImage by remember { mutableStateOf<modelNameUser?>(null) }
    LaunchedEffect(searchEmail) {
        getImageFriend(searchEmail) { image ->
            userImage = image
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(searchResults) { user ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        85.dp
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = user.email ?: "",
                        color = Color.Gray,
                        modifier = Modifier
                            .weight(0.4f)
                            .padding(start = 8.dp)
                    )
                    Text(
                        text = "${userImage?.firstName} ${userImage?.lastName}",
                        color = Color.White,
                        modifier = Modifier
                            .weight(0.6f)
                            .padding(start = 8.dp, top = 5.dp)
                    )
                }

                Button(
                    onClick = {

                    },
                    modifier = Modifier
                        .width(78.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor =  Color.Yellow,
                        contentColor =  Color.Black
                    ),
                ) {
                    Text("Add")
                }
            }
        }
    }
}



fun searchForUsers(email: String, onSuccess: (List<modelUser>) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")

    usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                val users = snapshot.children.mapNotNull { it.getValue(modelUser::class.java) }
                onSuccess(users)
            } else {
                onSuccess(emptyList())
            }
        }

        override fun onCancelled(error: DatabaseError) {
            onSuccess(emptyList())
        }
    })
}

@Composable
fun mainListFriend( useridModel: UserIDModel, userID: String, comback: () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111111))
    ) {

        headScreen(conback = comback)
        bodyListFriendScreen(userIdModel = useridModel,userid = userID)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewListFriend(){
    mainListFriend(comback = {}, useridModel = UserIDModel(), userID = "123")
//    listFriend(searchResults = listOf(modelUser("123","dfsb")),searchEmail = "123")
}